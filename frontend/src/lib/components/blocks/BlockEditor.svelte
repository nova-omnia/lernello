<script lang="ts">
	import { blockActionState } from '$lib/states/blockActionState.svelte';
	import BlockItem from '$lib/components/blocks/BlockItem.svelte';
	import { flip } from 'svelte/animate';
	import BlockSelectPopover from './BlockSelectPopover.svelte';
</script>

<div
	class="preset-filled-surface-50-950 border-surface-100-900 m-0 space-y-4 overflow-y-auto border-r p-4"
>
	<h1 class="h1">Learning Unit Details</h1>

	<div class="space-y-2">
		<BlockSelectPopover index={-1} />
		{#each blockActionState.blocks as block, index (block.uuid)}
			<div animate:flip={{ duration: 200 }} class="space-y-2">
				<BlockItem {block} />
				<BlockSelectPopover {index} />
			</div>
		{/each}
	</div>
</div>
