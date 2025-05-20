<script lang="ts">
	import { type DndEvent, dragHandleZone, TRIGGERS } from 'svelte-dnd-action';
	import { flip } from 'svelte/animate';
	import BlockReorderItem from '$lib/components/blocks/BlockOverviewItem.svelte';
	import { blockActionState, queueBlockAction } from '$lib/states/blockActionState.svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import { initializeInstructorDnd } from '$lib/states/dndService';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';
	import { CheckCircle, Loader2 } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';

	let { language, role } = $props();

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

	function scrollToBlock(blockId: string) {
		const element = document.getElementById(`block-${blockId}`);
		if (element) {
			element.scrollIntoView({ behavior: 'smooth', block: 'start' });
		}
	}
</script>

<div class="preset-filled-surface-50-950 mt-5 space-y-4 overflow-y-auto p-3">
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
		<button type="button" class="btn preset-outlined-surface-500 mb-8 w-full pointer-events-none cursor-default">
			{#if blockActionState.queue.length !== 0}
				<Loader2 class="h-5 w-5 animate-spin" />
				<span>{$_('block.saving')}</span>
			{:else}
				<CheckCircle class="h-5 w-5 text-green-600" />
				<span>{$_('block.saved')}</span>
			{/if}
		</button>

		{#if role === INSTRUCTOR_ROLE}
			{#each blocksSnapshot as block (block.uuid)}
				<div class="block" animate:flip={{ duration: 200 }}>
					<BlockReorderItem
						{block}
						role={INSTRUCTOR_ROLE}
						{language}
						scrollToBlock={() => scrollToBlock(block.uuid)}
					/>
				</div>
			{/each}
		{/if}
	</div>
</div>
