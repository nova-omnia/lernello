<script lang="ts">
	import { dragHandle } from 'svelte-dnd-action';
	import { GripVertical } from 'lucide-svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import BlockIconHeader from './BlockIconHeader.svelte';
	import { TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';

	interface BlockReorderItemProps {
		block: BlockRes;
		language: string;
	}
	const { block, language }: BlockReorderItemProps = $props();
</script>

<div
	class="card bg-surface-100-900 border-surface-200-800 flex items-center gap-1 border p-2 shadow transition-all duration-200"
>
	<div use:dragHandle aria-label={`drag-handle for ${block.name}`}>
		<GripVertical class="text-surface-400-600 h-6 w-6" />
	</div>
	<BlockIconHeader {block} role={TRAINEE_ROLE} {language} />
</div>
