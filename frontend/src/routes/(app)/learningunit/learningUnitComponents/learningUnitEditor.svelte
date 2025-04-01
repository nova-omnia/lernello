<script lang="ts">
	import Block from '../blocks/block.svelte';
	import { flip } from 'svelte/animate';
	import type { BlockItem } from '$lib/models/globalBlock';

	export let blocks: BlockItem[];
	export let flipDurationMs: number;
	export let unitId: string;
</script>

<div class="m-0 overflow-y-auto border-r border-gray-300 bg-white p-4">
	<h2 class="mb-4 text-xl font-bold">Learning Unit Details</h2>

	<div class="space-y-2">
		{#each blocks as block (block.uuid)}
			<div animate:flip={{ duration: flipDurationMs }}>
				<Block {block} {unitId} />
			</div>
		{/each}
	</div>
</div>
