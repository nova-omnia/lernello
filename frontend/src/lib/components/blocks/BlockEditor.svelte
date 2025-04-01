<script lang="ts">
	import { learningUnitActionQueue } from '$lib/states/blockActionState';
	import BlockItem from '$lib/components/blocks/BlockItem.svelte';
	import { flip } from 'svelte/animate';
</script>

<div class="preset-filled-surface-50-950 m-0 overflow-y-auto border-r border-gray-300 p-4">
	<h1 class="h1">Learning Unit Details</h1>

	<div class="space-y-2">
		{#each learningUnitActionQueue.blocks as block (block.uuid)}
			<div animate:flip={{ duration: 200 }}>
				<BlockItem {block} />
			</div>
		{/each}
	</div>
</div>
