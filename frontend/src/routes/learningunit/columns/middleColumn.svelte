<script lang="ts">
	import Block from '../blocks/block.svelte';
	import { flip } from 'svelte/animate';
	import type { BlockItem } from '../blocks/globalBlocks'; // adjust the path as needed

	export let blocks: BlockItem[];
	export let flipDurationMs: number;
	export let unitId: string;

	async function deleteBlock(blockId: string) {
		try {
			console.log('Deleting block...', blockId);
			const response = await fetch(`/api/learning-kit/learning-unit/${unitId}/blocks/${blockId}`, {
				method: 'DELETE'
			});
			if (!response.ok) {
				throw new Error('Failed to delete block');
			}
			blocks = blocks.filter((block) => block.uuid !== blockId);
			console.log('Block deleted successfully');
		} catch (error) {
			console.error('Error deleting block:', error);
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
