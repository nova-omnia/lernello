<script lang="ts">
	import { _ } from 'svelte-i18n';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import BlockIcon from './BlockIcon.svelte';

	interface BlockIconHeaderProps {
		block: BlockRes;
	}
	const { block }: BlockIconHeaderProps = $props();

	let blockTypeTerm = $derived.by(() => {
		switch (block.type) {
			case 'THEORY':
				return 'block.theoryBlock';
			case 'MULTIPLE_CHOICE':
				return 'block.multipleChoiceQuiz';
			case 'QUESTION':
				return 'block.questionBlock';
			default:
				return 'Unknown Block';
		}
	});
</script>

<div class="flex items-end gap-2">
	<BlockIcon iconType={block.type} />
	<h3 class="font-medium">{block.name}</h3>
	<span class="text-sm text-gray-500">({$_(blockTypeTerm)})</span>
</div>
