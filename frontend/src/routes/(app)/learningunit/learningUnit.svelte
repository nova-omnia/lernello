<script lang="ts">
	import { overrideItemIdKeyNameBeforeInitialisingDndZones } from 'svelte-dnd-action';

	import BlockSelector from '$lib/components/blocks/BlockSelector.svelte';
	import BlockEditor from '$lib/components/blocks/BlockEditor.svelte';
	import BlockReorder from '$lib/components/blocks/BlockReorder.svelte';
	import type { Block } from '$lib/models/block';
	import { blockActionState } from '$lib/states/blockActionState.svelte';

	overrideItemIdKeyNameBeforeInitialisingDndZones('uuid');

	interface LearningUnitProps {
		blocks: Block[];
	}
	const { blocks }: LearningUnitProps = $props();

	blockActionState.blocks = blocks;
	blockActionState.queue = [];
</script>

<div class="-m-4">
	<div class="grid h-full grid-cols-[25%_50%_25%]">
		<BlockSelector />
		<BlockEditor />
		<BlockReorder />
	</div>
</div>
