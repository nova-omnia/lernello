<script lang="ts">
	import Block from '../blocks/block.svelte';
	import { flip } from 'svelte/animate';
	import type { BlockItem } from '../blocks/globalBlocks';
	import { type ToastContext } from '@skeletonlabs/skeleton-svelte';
	import { getContext } from "svelte";
	const toast: ToastContext = getContext('toast');

	export let blocks: BlockItem[];
	export let flipDurationMs: number;
	export let unitId: string;

	async function deleteBlock(blockId: string) {
		try {
			toast.create({
				title: 'Deleting block...',
				description: 'Please wait',
				type: 'info'
			});
			const response = await fetch(`/api/learning-kit/learning-unit/${unitId}/blocks/${blockId}`, {
				method: 'DELETE'
			});
			if (!response.ok) {
				toast.create({
					title: 'Error',
					description: 'Failed to delete block',
					type: 'error'
				});
			}
			blocks = blocks.filter((block) => block.uuid !== blockId);
			toast.create({
				title: 'Success',
				description: 'Block deleted successfully',
				type: 'success'
			});
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

<div class="m-0 overflow-y-auto border-r border-gray-300 bg-white p-4">
	<h2 class="mb-4 text-xl font-bold">Learning Unit Details</h2>

	<div class="space-y-2">
		{#each blocks as block (block.uuid)}
			<div animate:flip={{ duration: flipDurationMs }}>
				<Block {block} onDelete={() => deleteBlock(block.uuid)} />
			</div>
		{/each}
	</div>
</div>