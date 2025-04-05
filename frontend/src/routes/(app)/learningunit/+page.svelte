<script lang="ts">
	import BlockEditor from '$lib/components/blocks/BlockEditor.svelte';
	import BlockReorder from '$lib/components/blocks/BlockReorder.svelte';
	import { blockActionState } from '$lib/states/blockActionState.svelte';

	let { data } = $props();

	blockActionState.blocks = data.blocks;
	blockActionState.queue = [];
</script>

<div class="-m-4">
	<div class="grid h-full grid-cols-[75%_25%]">
		<BlockEditor />
		<BlockReorder />
	</div>
</div>
