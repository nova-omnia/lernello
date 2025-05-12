<script lang="ts">
	import { blockActionState } from '$lib/states/blockActionState.svelte.js';
	import BlockItem from '$lib/components/blocks/BlockItem.svelte';
	import type { RoleType } from '$lib/schemas/response/UserInfo.js';
	import { _ } from 'svelte-i18n';
	import BlockOverviewItem from '$lib/components/blocks/BlockOverviewItem.svelte';

	interface LearningUnitTrainerProps {
		role: RoleType;
	}

	const { role }: LearningUnitTrainerProps = $props();

	function scrollToBlock(blockId: string) {
		const element = document.getElementById(`block-${blockId}`);
		if (element) {
			element.scrollIntoView({ behavior: 'smooth', block: 'start' });
		}
	}
</script>

<div class="grid h-full grid-cols-[75%_25%]">
	<div
		class="preset-filled-surface-50-950 border-surface-100-900 m-0 space-y-4 overflow-y-auto border-r p-4"
	>
		{#if blockActionState.blocks.length === 0}
			<p>{$_('learningUnit.noBlocks')}</p>
		{:else}
			{#each blockActionState.blocks as block (block.uuid)}
				<div id={`block-${block.uuid}`} class="block-wrapper">
					<BlockItem {block} {role} />
				</div>
			{/each}
		{/if}
	</div>
	{#if blockActionState.blocks.length > 0}
		<div class="preset-filled-surface-50-950 space-y-4 overflow-y-auto p-4">
			{#each blockActionState.blocks as block (block.uuid)}
				<BlockOverviewItem {block} {role} scrollToBlock={() => scrollToBlock(block.uuid)} />
			{/each}
		</div>
	{/if}
</div>
