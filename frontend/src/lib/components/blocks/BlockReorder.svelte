<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { type DndEvent, dragHandleZone, TRIGGERS } from 'svelte-dnd-action';
	import { flip } from 'svelte/animate';
	import BlockReorderItem from '$lib/components/blocks/BlockReorderItem.svelte';
	import { blockActionState, queueBlockAction } from '$lib/states/blockActionState.svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';

	let blocksSnapshot = $derived(
		blockActionState.blocks.map((block) => ({ ...block, id: block.uuid }))
	);

	type BlockWithId = BlockRes & { id: string };

	let currentlyDraggingId: string | null = null;
	function handleSortOnConsider(e: CustomEvent<DndEvent<BlockWithId>>) {
		blocksSnapshot = e.detail.items;
		if (e.detail.info.trigger === TRIGGERS.DRAG_STARTED) {
			currentlyDraggingId = e.detail.info.id;
		}
	}

	function handleSortOnFinalize(e: CustomEvent<DndEvent<BlockWithId>>) {
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
			data: {
				newIndex: newBlockIdx
			}
		});
	}
</script>

<div class="preset-filled-surface-50-950 space-y-4 overflow-y-auto p-4">
	<h2 class="h2">{$_('learningUnit.reorderBlocks')}</h2>
	<div
		class="space-y-2 rounded-lg"
		use:dragHandleZone={{
			items: blocksSnapshot,
			flipDurationMs: 200,
			dropFromOthersDisabled: true
		}}
		onconsider={handleSortOnConsider}
		onfinalize={handleSortOnFinalize}
	>
		{#each blocksSnapshot as block (block.id)}
			<div class="block" animate:flip={{ duration: 200 }}>
				<BlockReorderItem {block} />
			</div>
		{/each}
	</div>
</div>
