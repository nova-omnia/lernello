<script lang="ts">
	import { dragHandle } from 'svelte-dnd-action';
	import { GripVertical, SquareMinus, SquareCheckBig, SquareX, View } from 'lucide-svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import BlockIconHeader from './BlockIconHeader.svelte';
	import { INSTRUCTOR_ROLE, type RoleType, TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';
	import type {
		BlockProgressRes,
		MultipleChoiceBlockProgressRes,
		QuestionBlockProgressRes,
		TheoryBlockProgressRes
	} from '$lib/schemas/response/progress/BlockProgressResSchema';

	interface BlockReorderItemProps {
		block: BlockRes;
		role: RoleType;
		scrollToBlock?: () => void;
		progress?: BlockProgressRes;
		language: string;
	}

	const { block, role, scrollToBlock, progress, language }: BlockReorderItemProps = $props();

	const statusIconMap = {
		VIEWED: View,
		CORRECT: SquareCheckBig,
		FALSE: SquareX,
		ELSE: SquareMinus
	};

	let StatusIconComponent = $derived.by(() => {
		if (progress) {
			if (role === TRAINEE_ROLE) {
				if (progress.blockType === 'MULTIPLE_CHOICE') {
					const multipleChoiceProgress = progress as MultipleChoiceBlockProgressRes;
					if (multipleChoiceProgress.isCorrect) {
						return statusIconMap.CORRECT;
					} else if (multipleChoiceProgress.isCorrect === false) {
						return statusIconMap.FALSE;
					}
				} else if (progress.blockType === 'QUESTION') {
					const questionProgress = progress as QuestionBlockProgressRes;
					if (questionProgress.isCorrect) {
						return statusIconMap.CORRECT;
					} else if (questionProgress.isCorrect === false) {
						return statusIconMap.FALSE;
					}
				} else if (progress.blockType === 'THEORY') {
					const theoryProgress = progress as TheoryBlockProgressRes;
					if (theoryProgress.isViewed) {
						return statusIconMap.VIEWED;
					}
				}
			}
		}
		return statusIconMap.ELSE;
	});

	const statusColorClass = $derived.by(() => {
		const currentIcon = StatusIconComponent;
		if (currentIcon === statusIconMap.FALSE) {
			return 'badge preset-filled-error-500';
		} else if (currentIcon === statusIconMap.VIEWED || currentIcon === statusIconMap.CORRECT) {
			return 'badge preset-filled-success-500';
		} else {
			return 'badge bg-surface-300-700';
		}
	});
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
		class="card bg-surface-100-900 border-surface-200-800 hover:bg-surface-300-700 flex w-full cursor-pointer items-center gap-2 border p-2 text-left shadow transition-all duration-200"
		onclick={() => {
			if (scrollToBlock) {
				scrollToBlock();
			}
		}}
	>
		<BlockIconHeader {block} role={TRAINEE_ROLE} {language} />
		<span class={statusColorClass}>
			<StatusIconComponent class="text-surface-100-900 h-6 w-6" />
		</span>
	</button>
{/if}
