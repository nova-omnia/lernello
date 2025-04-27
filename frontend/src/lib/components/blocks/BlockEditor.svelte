<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { blockActionState } from '$lib/states/blockActionState.svelte';
	import BlockItem from '$lib/components/blocks/BlockItem.svelte';
	import { flip } from 'svelte/animate';
	import BlockSelectPopover from './BlockSelectPopover.svelte';

	interface BlockEditorProps {
		learningUnitId: string;
	}

	const { learningUnitId }: BlockEditorProps = $props();
</script>

<div
	class="preset-filled-surface-50-950 border-surface-100-900 m-0 space-y-4 overflow-y-auto border-r p-4"
>
	<div class="space-y-2">
		<BlockSelectPopover index={-1} {learningUnitId} />
		{#each blockActionState.blocks as block, index (block.uuid)}
			<div animate:flip={{ duration: 200 }} class="space-y-2">
				<BlockItem {block} {learningUnitId} />
				<BlockSelectPopover {index} {learningUnitId} />
			</div>
		{/each}
	</div>
</div>
