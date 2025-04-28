<script lang="ts">
	import { dragHandle } from 'svelte-dnd-action';
	import { GripVertical } from 'lucide-svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import BlockIconHeader from './BlockIconHeader.svelte';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';

	interface BlockReorderItemProps {
		block: BlockRes;
	}
	const { block }: BlockReorderItemProps = $props();
</script>

<div
	use:dragHandle
	aria-label={`drag-handle for ${block.name}`}
	class="card bg-surface-100-900 border-surface-200-800 flex items-center gap-1 border p-2 shadow transition-all duration-200"
>
	<GripVertical class="text-surface-400-600 h-6 w-6" />

	<BlockIconHeader {block} role={INSTRUCTOR_ROLE} />
</div>
