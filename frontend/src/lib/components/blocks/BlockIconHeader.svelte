<script lang="ts">
	import { _ } from 'svelte-i18n';
	import {
		type BlockRes,
		MULTIPLE_CHOICE_BLOCK_TYPE,
		QUESTION_BLOCK_TYPE,
		THEORY_BLOCK_TYPE
	} from '$lib/schemas/response/BlockRes';
	import BlockIcon from './BlockIcon.svelte';
	import { INSTRUCTOR_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';
	import BlockAiGenerationButton from '$lib/components/blocks/BlockAiGenerationButton.svelte';

	interface BlockIconHeaderProps {
		block: BlockRes;
		role: RoleType;
	}
	const { block, role }: BlockIconHeaderProps = $props();

	let blockTypeTerm = $derived.by(() => {
		switch (block.type) {
			case THEORY_BLOCK_TYPE:
				return 'block.theoryBlock';
			case MULTIPLE_CHOICE_BLOCK_TYPE:
				return 'block.multipleChoiceQuiz';
			case QUESTION_BLOCK_TYPE:
				return 'block.questionBlock';
			default:
				return 'Unknown Block';
		}
	});
</script>

<div class="flex justify-between">
	<div class="flex items-center gap-2">
		<BlockIcon iconType={block.type} />
		<div class="flex items-baseline gap-2">
			<h3 class="font-medium">{block.name}</h3>
			<span class="text-sm text-gray-500">({$_(blockTypeTerm)})</span>
		</div>
	</div>

	{#if role === INSTRUCTOR_ROLE}
		<div class="ml-auto">
			<BlockAiGenerationButton {block} />
		</div>
	{/if}
</div>
