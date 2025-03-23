<script lang="ts">
    import { onMount } from 'svelte';

    interface Block {
        uuid: string;
        name: string;
        type: string;
    }

    let blocks: Block[] = [
        { uuid: '1', name: 'Block A', type: 'theory' },
        { uuid: '2', name: 'Block B', type: 'quiz' }
    ];

    let originalOrder: string[] = [];
    let orderChanged = false;

    onMount(() => {
        originalOrder = blocks.map((b) => b.uuid);
    });

    function handleDragStart(event: DragEvent, index: number) {
        event.dataTransfer?.setData('text/plain', String(index));
    }

    function handleDragOver(event: DragEvent) {
        event.preventDefault();
    }

    function handleDrop(event: DragEvent, index: number) {
        const fromIndex = Number(event.dataTransfer?.getData('text/plain'));
        if (fromIndex === index) return;
        const [movedBlock] = blocks.splice(fromIndex, 1);
        blocks.splice(index, 0, movedBlock);
        blocks = blocks; // Reassign to trigger reactivity
        orderChanged = JSON.stringify(originalOrder) !== JSON.stringify(blocks.map((b) => b.uuid));
    }

    function saveOrder() {
        originalOrder = blocks.map((b) => b.uuid);
        orderChanged = false;
    }

    async function addTheoryBlock() {
        const response = await fetch('/api/blocks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: 'New Theory Block',
                type: 'theory'
            })
        });

        const newBlock = await response.json();
        blocks.push(newBlock);
        originalOrder = blocks.map((b) => b.uuid);
    }

    async function addQuizBlock() {
        const response = await fetch('/api/blocks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: 'New Quiz Block',
                type: 'quiz'
            })
        });

        const newBlock = await response.json();
        blocks.push(newBlock);
        originalOrder = blocks.map((b) => b.uuid);
    }
</script>

<div class="grid grid-cols-3 h-full">
    <!-- Left Column: Add Block Buttons -->
    <div class="p-4 bg-gray-30 border-r border-gray-300">
        <h2 class="text-lg font-bold mb-4">Actions</h2>
        <button
                type="button"
                class="mb-2 w-full py-2 px-4 rounded bg-primary-500 text-white hover:bg-primary-600"
                on:click={addTheoryBlock}
        >
            Add Theory Block
        </button>
        <button
                type="button"
                class="w-full py-2 px-4 rounded bg-primary-500 text-white hover:bg-primary-600"
                on:click={addQuizBlock}
        >
            Add Quiz Block
        </button>
    </div>

    <!-- Middle Column: Placeholder for Detailed View -->
    <div class="p-4 bg-white border-r border-gray-300">
        <h2 class="text-xl font-bold mb-4">Learning Unit Details</h2>
        <p>Placeholder for block details...</p>
    </div>

    <!-- Right Column: Block List with Reordering -->
    <div class="p-4 bg-gray-50">
        <h3 class="font-bold mb-2">Block Titles</h3>
        <ul class="space-y-1">
            {#each blocks as block, i}
                <li
                        class="bg-white border p-2 rounded cursor-move"
                        draggable="true"
                        on:dragstart={(e) => handleDragStart(e, i)}
                        on:dragover={handleDragOver}
                        on:drop={(e) => handleDrop(e, i)}
                >
                    {block.name} ({block.type})
                </li>
            {/each}
        </ul>
        {#if orderChanged}
            <button
                    class="mt-4 w-full py-2 px-4 bg-secondary-500 text-white rounded hover:bg-secondary-600"
                    on:click={saveOrder}
            >
                Save Order
            </button>
        {/if}
    </div>
</div>