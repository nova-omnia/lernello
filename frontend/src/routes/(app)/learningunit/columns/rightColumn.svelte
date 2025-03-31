<script lang="ts">
	import { dragHandleZone, type DndEvent } from 'svelte-dnd-action';
	import { flip } from 'svelte/animate';
	import BlockTitle from '../learningUnitComponents/blockTitle.svelte';
	import type { BlockItem } from '../blocks/globalBlocks'; // adjust the path as needed

	export let blocks: BlockItem[];
	export let flipDurationMs: number;
	export let unitId: string;

	let orderChanged: boolean = false;
	let initialOrder: string[] = blocks.map((block) => block.uuid);

	function handleSortOnConsider(e: CustomEvent<DndEvent<BlockItem>>) {
		initialOrder = blocks.map((block) => block.uuid);
		blocks = e.detail.items;
	}

	function handleSortOnFinalize(e: CustomEvent<DndEvent<BlockItem>>) {
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

	async function saveOrder() {
		try {
			console.log('Saving order...');
			const blockIds = blocks.map((block) => block.uuid);
			const response = await fetch(`/api/learning-kit/learning-unit/${unitId}/blocks/order`, {
				method: 'PUT',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(blockIds)
			});
			if (!response.ok) {
				throw new Error('Failed to update block order');
			}
			const updatedLearningUnit = await response.json();
			console.log('Updated Learning Unit:', updatedLearningUnit);
			orderChanged = false;
		} catch (error) {
			console.error('Error updating block order:', error);
		}
	}
</script>

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
