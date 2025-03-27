<script lang="ts">
    import { onMount } from 'svelte';
    import { flip } from 'svelte/animate';
    import { quintOut } from 'svelte/easing';

    let mouseStartY = 0;
    const blockHeight = 60; // Height of each block

    function handleMouseDown(e: MouseEvent, index: number, column: 'center' | 'right') {
        draggingIndex = index;
        draggingColumn = column;
        mouseStartY = e.clientY;
        window.addEventListener("mousemove", handleMouseMove);
        window.addEventListener("mouseup", handleMouseUp);
        e.preventDefault();
    }

    function handleMouseMove(e: MouseEvent) {
        if (draggingIndex === null) return;
        touchOffsetY = e.clientY - mouseStartY;
    }

    function handleMouseUp(e: MouseEvent) {
        if (draggingIndex === null) return;
        // Calculate the new position by using half the block height so that the block can slide in between others.
        const currentPos = (draggingIndex * blockHeight) + touchOffsetY + (blockHeight / 2);
        let targetIndex = Math.floor(currentPos / blockHeight);
        // Clamp target index to valid range
        if (targetIndex < 0) targetIndex = 0;
        if (targetIndex >= blocks.length) targetIndex = blocks.length - 1;

        if (targetIndex !== draggingIndex) {
            const [movedBlock] = blocks.splice(draggingIndex, 1);
            blocks.splice(targetIndex, 0, movedBlock);
            blocks = blocks; // trigger reactivity
            orderChanged = JSON.stringify(originalOrder) !== JSON.stringify(blocks.map((b) => b.uuid));
        }
        draggingIndex = null;
        draggingColumn = null;
        touchOffsetY = 0;
        window.removeEventListener("mousemove", handleMouseMove);
        window.removeEventListener("mouseup", handleMouseUp);
    }

    interface Block {
        uuid: string;
        name: string;
        type: string;
    }

    let blocks: Block[] = [
        { uuid: '1', name: 'Block A', type: 'theory' },
        { uuid: '2', name: 'Block B', type: 'quiz' },
        { uuid: '3', name: 'Block C', type: 'theory' },
        { uuid: '4', name: 'Block D', type: 'quiz' },
    ];

    let openSections = {
        theory: true,
        quiz: true,
    };

    interface BlockGroup {
        type: 'theory' | 'quiz';
        label: string;
    }

    const blockGroups: BlockGroup[] = [
        { type: 'quiz', label: 'Quiz Blocks' },
    ];

    const toggleSection = (type: 'theory' | 'quiz') => {
        openSections = { ...openSections, [type]: !openSections[type] };
    };

    let originalOrder: string[] = [];
    let orderChanged = false;

    onMount(() => {
        originalOrder = blocks.map((b) => b.uuid);
    });

    let draggingIndex: number | null = null;
    let draggingColumn: 'center' | 'right' | null = null;
    let touchStartY = 0;
    let touchOffsetY = 0;

    function handleTouchStart(event: TouchEvent, index: number, column: 'center' | 'right') {
        touchStartY = event.touches[0].clientY;
        draggingIndex = index;
        draggingColumn = column;
    }

    function handleTouchMove(event: TouchEvent) {
        if (draggingIndex === null) return;
        touchOffsetY = event.touches[0].clientY - touchStartY;
        event.preventDefault();
    }

    function handleTouchEnd() {
        if (draggingIndex === null) return;

        const targetIndex = draggingIndex + Math.round(touchOffsetY / 60);
        if (targetIndex >= 0 && targetIndex < blocks.length && targetIndex !== draggingIndex) {
            const [movedBlock] = blocks.splice(draggingIndex, 1);
            blocks.splice(targetIndex, 0, movedBlock);
            blocks = blocks;
            orderChanged = JSON.stringify(originalOrder) !== JSON.stringify(blocks.map((b) => b.uuid));
        }

        draggingIndex = null;
        draggingColumn = null;
        touchOffsetY = 0;
    }

    function deleteBlock(index: number) {
        blocks.splice(index, 1);
        blocks = blocks;
        orderChanged = JSON.stringify(originalOrder) !== JSON.stringify(blocks.map((b) => b.uuid));
    }

    async function addBlock(name: string, type: 'theory' | 'quiz') {
        const response = await fetch('/api/blocks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name, type })
        });

        const newBlock = await response.json();
        blocks.push(newBlock);
        originalOrder = blocks.map((b) => b.uuid);
    }

    function saveOrder() {
        originalOrder = blocks.map((b) => b.uuid);
        orderChanged = false;
    }

    const addTheoryBlock = () => addBlock('New Theory Block', 'theory');
    const addMultipleChoiceQuizBlock = () => addBlock('New Multiple Choice Block', 'quiz');
    const addTextAnswerQuizBlock = () => addBlock('New Text Answer Block', 'quiz');
</script>

<div class="grid grid-cols-[25%_50%_25%] h-screen">
    <!-- Left Column: Add Block Buttons -->
    <div class="p-4 bg-gray-30 border-r border-gray-300">
        <h2 class="text-lg font-bold mb-4">Actions</h2>

        <button
                type="button"
                class="w-full py-2 px-4 rounded-xl bg-primary-500 text-white hover:bg-primary-600"
                on:click={addTheoryBlock}
        >
            Theory Block
        </button>

        {#each blockGroups as group}
            <div class="mb-4 py-10">
                <button
                        class="w-full flex justify-between items-center p-2 bg-white rounded-t-lg hover:bg-gray-200"
                        on:click={() => toggleSection(group.type)}
                        aria-expanded={openSections[group.type]}
                >
                    <h3 class="font-bold">{group.label}</h3>
                    <span class="text-lg">
                        {openSections[group.type] ? '−' : '+'}
                    </span>
                </button>

                {#if openSections[group.type]}
                    <div class="p-2 bg-white border-t-0 rounded-b-lg space-y-2">
                        {#if group.type === 'quiz'}
                            <button
                                    type="button"
                                    class="w-full py-2 px-4 rounded-xl bg-primary-500 text-white hover:bg-primary-600"
                                    on:click={addMultipleChoiceQuizBlock}
                            >
                                Multiple Choice Quiz
                            </button>
                            <button
                                    type="button"
                                    class="w-full py-2 px-4 rounded-xl bg-primary-500 text-white hover:bg-primary-600"
                                    on:click={addTextAnswerQuizBlock}
                            >
                                Text Answer Quiz
                            </button>
                        {/if}
                    </div>
                {/if}
            </div>
        {/each}
    </div>

    <!-- Middle Column: Block Frames -->
    <div class="p-4 bg-white border-r border-gray-300 overflow-y-auto">
        <h2 class="text-xl font-bold mb-4">Learning Unit Details</h2>

        <div class="space-y-4">
            {#each blocks as block, i (block.uuid)}
                <div
                        class="relative group bg-white rounded-lg border border-gray-200 p-4 transition-all duration-200 hover:border-primary-500"
                        animate:flip={{ delay: 50, duration: 300, easing: quintOut }}
                        style={draggingIndex === i && draggingColumn === 'center'
            ? `transform: translateY(${touchOffsetY}px); opacity: 0.9; z-index: 100;`
            : ''}
                >
                    <!-- Delete Button -->
                    <button
                            class="absolute -right-2 -top-2 p-1 bg-red-500 text-white rounded-full opacity-0 group-hover:opacity-100 transition-opacity"
                            on:click={() => deleteBlock(i)}
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
                        </svg>
                    </button>
                    <!-- Drag Handle -->
                    <button
                            class="absolute -left-2 top-1/2 -translate-y-1/2 p-1 text-gray-400 hover:text-primary-500 cursor-grab active:cursor-grabbing"
                            on:mousedown={(e) => handleMouseDown(e, i, 'center')}
                            on:touchstart={(e) => handleTouchStart(e, i, 'center')}
                            on:touchmove={handleTouchMove}
                            on:touchend={handleTouchEnd}
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                            <path d="M7 2a2 2 0 1 0 .001 4.001A2 2 0 0 0 7 2zm0 6a2 2 0 1 0 .001 4.001A2 2 0 0 0 7 8zm0 6a2 2 0 1 0 .001 4.001A2 2 0 0 0 7 14zm6-8a2 2 0 1 0-.001-4.001A2 2 0 0 0 13 6zm0 2a2 2 0 1 0 .001 4.001A2 2 0 0 0 13 8zm0 6a2 2 0 1 0 .001 4.001A2 2 0 0 0 13 14z" />
                        </svg>
                    </button>
                    <!-- Block Content -->
                    <div class="flex items-center h-full pl-6">
                        <h3 class="font-medium">{block.name}</h3>
                        <span class="ml-2 text-sm text-gray-500">({block.type})</span>
                    </div>
                </div>
            {/each}
        </div>
    </div>

    <!-- Right Column: Block List with Reordering -->
    <div class="p-4 bg-gray-50 overflow-y-auto">
        <h3 class="font-bold mb-4">Block Titles</h3>
        <div class="space-y-2">
            {#each blocks as block, i (block.uuid)}
                <div
                        class="relative group bg-white rounded-lg border border-gray-200 p-3 transition-all duration-200 hover:border-primary-500"
                        animate:flip={{ delay: 50, duration: 300, easing: quintOut }}
                        style={draggingIndex === i && draggingColumn === 'right'
            ? `transform: translateY(${touchOffsetY}px); opacity: 0.9; z-index: 100;`
            : ''}
                >
                    <!-- Drag Handle -->
                    <button
                            class="absolute -left-2 top-1/2 -translate-y-1/2 p-1 text-gray-400 hover:text-primary-500 cursor-grab active:cursor-grabbing"
                            on:mousedown={(e) => handleMouseDown(e, i, 'right')}
                            on:touchstart={(e) => handleTouchStart(e, i, 'right')}
                            on:touchmove={handleTouchMove}
                            on:touchend={handleTouchEnd}
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                            <path d="M7 2a2 2 0 1 0 .001 4.001A2 2 0 0 0 7 2zm0 6a2 2 0 1 0 .001 4.001A2 2 0 0 0 7 8zm0 6a2 2 0 1 0 .001 4.001A2 2 0 0 0 7 14zm6-8a2 2 0 1 0-.001-4.001A2 2 0 0 0 13 6zm0 2a2 2 0 1 0 .001 4.001A2 2 0 0 0 13 8zm0 6a2 2 0 1 0 .001 4.001A2 2 0 0 0 13 14z" />
                        </svg>
                    </button>
                    <!-- Block Content -->
                    <div class="flex items-center h-full pl-6">
                        <h3 class="font-medium">{block.name}</h3>
                        <span class="ml-2 text-sm text-gray-500">({block.type})</span>
                    </div>
                </div>
            {/each}
        </div>
        {#if orderChanged}
            <button
                    class="mt-4 w-full py-2 px-4 bg-secondary-500 text-white rounded-xl hover:bg-secondary-600"
                    on:click={saveOrder}
            >
                Save Order
            </button>
        {/if}
    </div>
</div>

<style>
    .drag-handle {
        touch-action: none;
    }
</style>