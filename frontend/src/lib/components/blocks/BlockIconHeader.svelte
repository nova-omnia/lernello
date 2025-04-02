<script lang="ts">
	import type { Block } from '$lib/models/block';
	import BlockIcon from './BlockIcon.svelte';

	interface BlockIconHeaderProps {
		block: Block;
	}
	const { block }: BlockIconHeaderProps = $props();
</script>

<div class="flex items-end gap-2">
	<BlockIcon iconType={block.type} />
	<h3 class="font-medium">{block.name}</h3>
	<span class="text-sm text-gray-500">({block.type})</span>
</div>
