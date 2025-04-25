<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { blockActionState } from '$lib/states/blockActionState.svelte';
	import BlockItem from '$lib/components/blocks/BlockItem.svelte';
	import { flip } from 'svelte/animate';

	interface BlockEditorProps {
		learningUnitId: string;
	}

	const { learningUnitId }: BlockEditorProps = $props();
</script>

<div
	class="preset-filled-surface-50-950 border-surface-100-900 m-0 space-y-4 overflow-y-auto border-r p-4"
>
	<h1 class="h1">{$_('learningUnit.details')}</h1>

	<div class="space-y-2">
		{#each blockActionState.blocks as block (block.uuid)}
			<div animate:flip={{ duration: 200 }} class="space-y-2">
				<BlockItem {block} />
			</div>
		{/each}
	</div>
</div>
