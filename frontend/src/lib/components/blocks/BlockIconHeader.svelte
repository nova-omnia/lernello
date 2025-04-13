<script lang="ts">
	import { _ } from 'svelte-i18n';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import BlockIcon from './BlockIcon.svelte';

	interface BlockIconHeaderProps {
		block: BlockRes;
	}
	const { block }: BlockIconHeaderProps = $props();

	let blockType: string = $state(block.type);

	switch (block.type) {
		case 'THEORY':
			blockType = 'block.theoryBlock';
			break;
		case 'MULTIPLE_CHOICE':
			blockType = 'block.multipleChoiceQuiz';
			break;
		default:
			blockType = 'Unknown Block';
	}
</script>

<div class="flex items-end gap-2">
	<BlockIcon iconType={block.type} />
	<h3 class="font-medium">{block.name}</h3>
	<span class="text-sm text-gray-500">({$_(blockType)})</span>
</div>
