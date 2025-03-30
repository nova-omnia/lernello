<script lang="ts">
	import {
		dragHandleZone,
		overrideItemIdKeyNameBeforeInitialisingDndZones,
		type DndEvent
	} from 'svelte-dnd-action';
	overrideItemIdKeyNameBeforeInitialisingDndZones('uuid');
	import { flip } from 'svelte/animate';
	import type { Component } from 'svelte';
	import TheoryBlockComponent from './blocks/theoryBlockComponent.svelte';
	import QuizBlockComponent from './blocks/quizBlockComponent.svelte';
	import BlockTitle from './learningUnitComponents/blockTitle.svelte';
	import Block from './blocks/block.svelte';

	const flipDurationMs = 200;

	let openSections = {
		theory: true,
		quiz: true
	};

	interface BlockGroup {
		type: 'theory' | 'quiz';
		label: string;
	}

	const blockGroups: BlockGroup[] = [{ type: 'quiz', label: 'Quiz Blocks' }];

	const toggleSection = (type: 'theory' | 'quiz') => {
		openSections = { ...openSections, [type]: !openSections[type] };
	};

	async function addBlock(name: string, type: 'theory' | 'quiz') {
		const response = await fetch('/api/blocks', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ name, type })
		});

		const newBlock = await response.json();
		blocks.push(newBlock);
	}

	const addTheoryBlock = () => addBlock('New Theory Block', 'theory');
	const addMultipleChoiceQuizBlock = () => addBlock('New Multiple Choice Block', 'quiz');
	const addTextAnswerQuizBlock = () => addBlock('New Text Answer Block', 'quiz');

	function deleteBlock(index: number) {
		blocks.splice(index, 1);
		blocks = blocks;
	}

	function saveOrder() {
		console.log('Saving order...');
		orderChanged = false;
	}

	interface Block {
		uuid: string;
		name: string;
		type: string;
		element: Component;
	}

	let blocks: Block[] = [
		{ uuid: '1', name: 'Block 1', type: 'theory', element: TheoryBlockComponent },
		{ uuid: '2', name: 'Block 2', type: 'quiz', element: QuizBlockComponent },
		{ uuid: '3', name: 'Block 3', type: 'theory', element: TheoryBlockComponent },
		{ uuid: '4', name: 'Block 4', type: 'quiz', element: QuizBlockComponent }
	];

	let orderChanged: boolean = false;
	let initialOrder: string[] = blocks.map((block) => block.uuid);

	function handleSortOnConsider(e: CustomEvent<DndEvent<Block>>) {
		initialOrder = blocks.map((block) => block.uuid);
		blocks = e.detail.items;
	}

	function handleSortOnFinalize(e: CustomEvent<DndEvent<Block>>) {
		const newBlocks = e.detail.items;
		const newOrder = newBlocks.map((block) => block.uuid);
		if (!arraysEqual(initialOrder, newOrder)) {
			orderChanged = true;
		}
		blocks = newBlocks;
		console.log(blocks);
	}

	function arraysEqual(arr1: string[], arr2: string[]): boolean {
		return arr1.length === arr2.length && arr1.every((val, idx) => val === arr2[idx]);
	}
</script>

<div class="grid h-screen grid-cols-[25%_50%_25%]">
	<!-- Left Column: Add Block Buttons -->
	<div class="bg-gray-30 border-r border-gray-300 p-4">
		<h2 class="mb-4 text-lg font-bold">Actions</h2>

		<button
			type="button"
			class="bg-primary-500 hover:bg-primary-600 w-full rounded-xl px-4 py-2 text-white"
			onclick={addTheoryBlock}
		>
			Theory Block
		</button>

		{#each blockGroups as group (group.type)}
			<div class="mb-4 py-10">
				<button
					class="flex w-full items-center justify-between rounded-t-lg bg-white p-2 hover:bg-gray-200"
					onclick={() => toggleSection(group.type)}
					aria-expanded={openSections[group.type]}
				>
					<span role="heading" aria-level="3" class="font-bold">{group.label}</span>
					<span class="text-lg">
						{openSections[group.type] ? '−' : '+'}
					</span>
				</button>

				{#if openSections[group.type]}
					<div class="space-y-2 rounded-b-lg border-t-0 bg-white p-2">
						{#if group.type === 'quiz'}
							<button
								type="button"
								class="bg-primary-500 hover:bg-primary-600 w-full rounded-xl px-4 py-2 text-white"
								onclick={addMultipleChoiceQuizBlock}
							>
								Multiple Choice Quiz
							</button>
							<button
								type="button"
								class="bg-primary-500 hover:bg-primary-600 w-full rounded-xl px-4 py-2 text-white"
								onclick={addTextAnswerQuizBlock}
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
	<div class="m-0 overflow-y-auto border-r border-gray-300 bg-white p-4">
		<h2 class="mb-4 text-xl font-bold">Learning Unit Details</h2>

		<div class="space-y-2">
			{#each blocks as block (block.uuid)}
				<div animate:flip={{ duration: flipDurationMs }}>
					<Block {block} onDelete={() => deleteBlock(blocks.indexOf(block))} />
				</div>
			{/each}
		</div>
	</div>

	<!-- Right Column: Block List with Reordering -->
	<div class="overflow-y-auto bg-gray-50 p-4">
		<h3 class="mb-4 font-bold">Block Titles</h3>
		<div
			class="space-y-2"
			use:dragHandleZone={{
				items: blocks,
				flipDurationMs,
				dropTargetStyle: { border: '2px dashed #ccc', borderRadius: '7px' },
				dropFromOthersDisabled: true
			}}
			onconsider={handleSortOnConsider}
			onfinalize={handleSortOnFinalize}
		>
			{#each blocks as block (block.uuid)}
				<div class="block" animate:flip={{ duration: flipDurationMs }}>
					<BlockTitle {block} />
				</div>
			{/each}
		</div>
		{#if orderChanged}
			<button
				class="bg-secondary-500 hover:bg-secondary-600 mt-4 w-full rounded-xl px-4 py-2 text-white"
				onclick={saveOrder}
			>
				Save Order
			</button>
		{/if}
	</div>
</div>
