<script lang="ts">
	import { type DndEvent, dragHandleZone } from 'svelte-dnd-action';
	import { flip } from 'svelte/animate';
	import BlockTitle from './blockTitle.svelte';
	import type { BlockItem } from '$lib/models/globalBlock';
	import { type ToastContext } from '@skeletonlabs/skeleton-svelte';
	import { getContext } from 'svelte';

	const toast: ToastContext = getContext('toast');

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
				toast.create({
					title: 'Error',
					description: 'Failed to update block order',
					type: 'error'
				});
			}
			toast.create({
				title: 'Success',
				description: 'Block order updated successfully',
				type: 'success'
			});
			orderChanged = false;
		} catch (error: unknown) {
			let statusCode = '';
			if (error && typeof error === 'object' && 'result' in error) {
				const errObj = error as { result: { status: string } };
				statusCode = errObj.result.status;
			}
			toast.create({
				title: 'Error',
				description: `Uh oh, something went wrong. (${statusCode})`,
				type: 'error'
			});
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
