<script lang="ts">
	import { type DndEvent, dragHandleZone, TRIGGERS } from 'svelte-dnd-action';
	import { flip } from 'svelte/animate';
	import BlockReorderItem from '$lib/components/blocks/BlockReorderItem.svelte';
	import { blockActionState, queueBlockAction } from '$lib/states/blockActionState.svelte';
	import type { Block } from '$lib/models/block';

	let blocksSnapshot = $derived(blockActionState.blocks);

	let currentlyDraggingId: string | null = null;
	function handleSortOnConsider(e: CustomEvent<DndEvent<Block>>) {
		blocksSnapshot = e.detail.items;
		if (e.detail.info.trigger === TRIGGERS.DRAG_STARTED) {
			currentlyDraggingId = e.detail.info.id;
		}
	}

	function handleSortOnFinalize(e: CustomEvent<DndEvent<Block>>) {
		if (!currentlyDraggingId) {
			throw new Error('No currently dragging ID');
		}
		const newBlockId = currentlyDraggingId;
		const newBlockIdx = e.detail.items.findIndex((block) => block.uuid === newBlockId);

		blocksSnapshot = e.detail.items;

		queueBlockAction({
			type: 'REORDER_BLOCK',
			data: {
				blockId: newBlockId,
				newIndex: newBlockIdx
			}
		});
	}
</script>

<div class="preset-filled-surface-50-950 space-y-4 overflow-y-auto p-4">
	<h2 class="h2">Reorder Blocks</h2>
	<div
		class="space-y-2"
		use:dragHandleZone={{
			items: blocksSnapshot,
			flipDurationMs: 200,
			dropTargetStyle: { border: '2px dashed #ccc', borderRadius: '7px' },
			dropFromOthersDisabled: true
		}}
		onconsider={handleSortOnConsider}
		onfinalize={handleSortOnFinalize}
	>
		{#each blocksSnapshot as block (block.uuid)}
			<div class="block" animate:flip={{ duration: 200 }}>
				<BlockReorderItem {block} />
			</div>
		{/each}
	</div>
</div>
