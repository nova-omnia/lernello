<script lang="ts">
	import { dragHandle } from 'svelte-dnd-action';
	import { GripVertical } from 'lucide-svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import BlockIconHeader from './BlockIconHeader.svelte';
	import { INSTRUCTOR_ROLE, type RoleType, TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';

	interface BlockReorderItemProps {
		block: BlockRes;
		role: RoleType;
		scrollToBlock?: () => void;
		language: string;
	}

	const { block, role, scrollToBlock, language }: BlockReorderItemProps = $props();
</script>

{#if role === INSTRUCTOR_ROLE}
	<div
		class="card bg-surface-100-900 border-surface-200-800 flex items-center gap-1 border p-2 shadow transition-all duration-200"
	>
		<div use:dragHandle aria-label={`drag-handle for ${block.name}`}>
			<GripVertical class="text-surface-400-600 h-6 w-6" />
		</div>
		<BlockIconHeader {block} role={TRAINEE_ROLE} {language} />
	</div>
{:else if role === TRAINEE_ROLE}
	<button
		type="button"
		class="card bg-surface-100-900 border-surface-200-800 hover:bg-surface-300-700 flex w-full cursor-pointer items-center gap-1 border p-2 text-left shadow transition-all duration-200"
		onclick={() => {
			if (scrollToBlock) {
				scrollToBlock();
			}
		}}
	>
		<BlockIconHeader {block} role={TRAINEE_ROLE} {language} />
	</button>
{/if}
