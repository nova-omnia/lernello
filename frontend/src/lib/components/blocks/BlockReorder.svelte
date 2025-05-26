<script lang="ts">
	import { dndzone } from 'svelte-dnd-action';
	import BlockReorderItem from '$lib/components/blocks/BlockOverviewItem.svelte';
	import { blockActionState, queueBlockAction } from '$lib/states/blockActionState.svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';
	import { CheckCircle, Loader2 } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';

	let { language, role } = $props();

	let blocks = $state([...blockActionState.blocks]);

	$effect(() => {
		blocks = [...blockActionState.blocks];
	});

	function handleReorder(event: CustomEvent) {
		const { items, info } = event.detail;

		const reordered = items as BlockRes[];
		blocks = reordered;

		if (event.type !== 'finalize') return;

		const newIndex = reordered.findIndex((b) => b.uuid === info.id);

		if (newIndex !== -1) {
			queueBlockAction({
				type: 'REORDER_BLOCK',
				blockId: info.id,
				newIndex,
				language
			});
		}
	}

	function scrollToBlock(blockId: string) {
		const element = document.getElementById(`block-${blockId}`);
		element?.scrollIntoView({ behavior: 'smooth', block: 'start' });
	}
</script>

<div class="preset-filled-surface-50-950 mt-0 space-y-4 overflow-y-auto pl-4">
	<button
		type="button"
		class="btn preset-outlined-surface-500 pointer-events-none mb-6 w-full cursor-default"
	>
		{#if blockActionState.queue.length !== 0}
			<Loader2 class="h-5 w-5 animate-spin" />
			<span>{$_('block.saving')}</span>
		{:else}
			<CheckCircle class="h-5 w-5 text-green-600" />
			<span>{$_('block.saved')}</span>
		{/if}
	</button>

	{#if role === INSTRUCTOR_ROLE}
		<div
			use:dndzone={{
				items: blocks,
				flipDurationMs: 200,
				dropTargetStyle: {
					outline: '1px dashed oklch(45.77% 0.07 211.76deg)',
					borderRadius: '.5rem'
				}
			}}
			onconsider={handleReorder}
			onfinalize={handleReorder}
			class="space-y-2 rounded-lg border-transparent"
		>
			{#each blocks as block (block.uuid)}
				<BlockReorderItem
					{block}
					role={INSTRUCTOR_ROLE}
					{language}
					scrollToBlock={() => scrollToBlock(block.uuid)}
				/>
			{/each}
		</div>
	{/if}
</div>
