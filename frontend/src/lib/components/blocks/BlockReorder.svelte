<script lang="ts">
	import { type DndEvent, dragHandleZone, TRIGGERS } from 'svelte-dnd-action';
	import { flip } from 'svelte/animate';
	import BlockReorderItem from '$lib/components/blocks/BlockReorderItem.svelte';
	import { blockActionState, queueBlockAction } from '$lib/states/blockActionState.svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import { initializeInstructorDnd } from '$lib/states/dndService';

	initializeInstructorDnd();

	let blocksSnapshot = $derived(blockActionState.blocks);
	let currentlyDraggingId: string | null = null;

	function handleSortOnConsider(e: CustomEvent<DndEvent<BlockRes>>) {
		blocksSnapshot = e.detail.items;
		if (e.detail.info.trigger === TRIGGERS.DRAG_STARTED) {
			currentlyDraggingId = e.detail.info.id;
		}
	}

	function handleSortOnFinalize(e: CustomEvent<DndEvent<BlockRes>>) {
		if (!currentlyDraggingId) {
			throw new Error('No currently dragging ID');
		}
		const newBlockId = currentlyDraggingId;
		const newBlockIdx = e.detail.items.findIndex((block) => block.uuid === newBlockId);
		const oldBlockIdx = blockActionState.blocks.findIndex((block) => block.uuid === newBlockId);
		if (newBlockIdx === oldBlockIdx) {
			return;
		}

		blocksSnapshot = e.detail.items;

		queueBlockAction({
			type: 'REORDER_BLOCK',
			blockId: newBlockId,
			newIndex: newBlockIdx
		});
	}
</script>

<div class="preset-filled-surface-50-950 space-y-4 overflow-y-auto p-4" style="margin-top: 34px;">
	<div
		class="space-y-2 rounded-lg"
		use:dragHandleZone={{
			items: blocksSnapshot,
			flipDurationMs: 200,
			dropFromOthersDisabled: true,
			dropTargetStyle: { outline: '1px dashed oklch(45.77% 0.07 211.76deg)', borderRadius: '.5rem' }
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
