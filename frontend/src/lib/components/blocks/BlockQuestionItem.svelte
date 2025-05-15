<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { Check, RotateCcw, X } from 'lucide-svelte';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import { type BlockRes, QUESTION_BLOCK_TYPE } from '$lib/schemas/response/BlockRes';
	import { type RoleType } from '$lib/schemas/response/UserInfo';
	import { createDebounced } from '$lib/utils/createDebounced';
	import EditPreviewTabContainer from '$lib/components/blocks/EditPreviewTabContainer.svelte';
	import { createMutation } from '@tanstack/svelte-query';
	import type { CheckQuestionAnswer } from '$lib/schemas/request/progress/CheckQuestionAnswerSchema';
	import { api } from '$lib/api/apiClient';
	import { checkQuestionAnswer } from '$lib/api/collections/progress';
	import { toaster } from '$lib/states/toasterState.svelte';
	import { learningUnitProgressState } from '$lib/states/LearningUnitProgressState.svelte';
	import type { QuestionBlockProgressRes } from '$lib/schemas/response/progress/BlockProgressResSchema';

	interface BlockQuestionItemProps {
		block: Extract<BlockRes, { type: typeof QUESTION_BLOCK_TYPE }>;
		role: RoleType;
		language: string;
	}

	const { block, role, language }: BlockQuestionItemProps = $props();

	let progress = $derived(
		learningUnitProgressState.getBlockProgress(block.uuid) as QuestionBlockProgressRes | undefined
	);

	$effect(() => {
		if (role === 'TRAINEE' && progress) {
			if (progress.lastAnswer !== undefined && progress.lastAnswer !== null) {
				traineeAnswer = progress.lastAnswer;
				if (progress.lastAnswer.trim() !== '') {
					isSubmitted = true;
					isCorrect = progress.isCorrect ?? null;
				} else {
					isSubmitted = false;
					isCorrect = null;
				}
			} else {
				traineeAnswer = '';
				isSubmitted = false;
				isCorrect = null;
			}
		} else if (role === 'TRAINEE' && !progress) {
			traineeAnswer = '';
			isSubmitted = false;
			isCorrect = null;
		}
	});

	let currentQuestion = $derived(
		block.translatedContents.find((content) => content.language == language)?.question ??
			block.question
	);
	let currentExpectedAnswer = $derived(
		block.translatedContents.find((content) => content.language == language)?.expectedAnswer ??
			block.expectedAnswer
	);

	let traineeAnswer = $state('');
	let isSubmitted = $state(false);
	let isCorrect = $state<boolean | null>(null);

	let blockId: string = $derived(
		block.translatedContents.find((content) => content.language == language)?.id ?? block.uuid
	);

	const onUpdateHandler = createDebounced(() => {
		let localBlockQuestion =
			block.translatedContents.find((content) => content.language == language)?.question ??
			block.question;
		let localExpectedAnswer =
			block.translatedContents.find((content) => content.language == language)?.expectedAnswer ??
			block.expectedAnswer;
		if (currentQuestion !== localBlockQuestion || currentExpectedAnswer !== localExpectedAnswer) {
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId,
				question: currentQuestion,
				expectedAnswer: currentExpectedAnswer
			});
		}
	}, 500);

	const checkAnswerMutation = createMutation({
		mutationFn: (payload: CheckQuestionAnswer) =>
			api(fetch).req(checkQuestionAnswer, payload).parse(),
		onSuccess: (data) => {
			isCorrect = data.isCorrect;
			isSubmitted = true;
			const blockProgress = learningUnitProgressState.getBlockProgress(block.uuid);
			learningUnitProgressState.updateBlockProgress(block.uuid, {
				...blockProgress,
				lastAnswer: traineeAnswer,
				isCorrect: data.isCorrect
			} as QuestionBlockProgressRes);
		},
		onError: (error) => {
			console.error('Error checking question answer:', error);
			toaster.create({
				description: $_('error.description', { values: { status: 'unknown' } }),
				type: 'error'
			});
			isSubmitted = true;
			isCorrect = null;
		}
	});

	function handleSubmit() {
		if (traineeAnswer.trim() !== '') {
			$checkAnswerMutation.mutate({ blockId, answer: traineeAnswer });
		}
	}
</script>

<EditPreviewTabContainer {role}>
	{#snippet editHeaderContent()}
		<!-- Empty -->
	{/snippet}

	{#snippet edit()}
		<div class="space-y-4">
			<input
				type="text"
				placeholder={$_('block.typeTheAnswer')}
				bind:value={currentQuestion}
				oninput={onUpdateHandler}
				onblur={onUpdateHandler}
				class="input w-full border p-2 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400"
			/>
			<input
				type="text"
				placeholder={$_('common.block.answer')}
				bind:value={currentExpectedAnswer}
				oninput={onUpdateHandler}
				onblur={onUpdateHandler}
				class="input w-full border p-2 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400"
			/>
		</div>
	{/snippet}

	{#snippet preview()}
		<div class="space-y-4">
			<div class="flex items-start justify-between gap-4">
				<div class="flex items-center gap-2">
					<h3 class="text-lg font-semibold dark:text-gray-200">{currentQuestion}</h3>
					{#if isSubmitted}
						<button
							type="button"
							onclick={() => {
								traineeAnswer = '';
								isSubmitted = false;
								isCorrect = null;
							}}
							class="btn bg-gray-200 text-gray-800 hover:bg-gray-300 dark:bg-gray-700 dark:text-white dark:hover:bg-gray-600"
							title={$_('common.reset')}
						>
							<RotateCcw size={16} />
						</button>
					{/if}
				</div>
				{#if !isSubmitted}
					<button
						type="button"
						onclick={handleSubmit}
						disabled={traineeAnswer.trim() === '' || $checkAnswerMutation.isPending}
						class="btn preset-filled whitespace-nowrap disabled:cursor-not-allowed disabled:opacity-50"
						title={$_('common.submit')}
					>
						{#if $checkAnswerMutation.isPending}
							{$_('common.submitting')}...
						{:else}
							{$_('common.submit')}
						{/if}
					</button>
				{/if}
			</div>

			<div class="flex items-end gap-4">
				<div class="flex-grow">
					<label
						for={`student-answer-${blockId}`}
						class="mb-1 block text-sm font-medium text-gray-700 dark:text-gray-300"
					>
						{$_('block.yourAnswer')}
					</label>
					<input
						id={`student-answer-${blockId}`}
						type="text"
						placeholder={$_('block.typeYourAnswer')}
						bind:value={traineeAnswer}
						disabled={isSubmitted || $checkAnswerMutation.isPending}
						class="input w-full border p-2 disabled:cursor-not-allowed disabled:opacity-60 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400"
					/>
				</div>
			</div>

			{#if isSubmitted}
				<div
					class={`rounded-lg border p-3 ${
						isCorrect === true
							? 'border-green-300 bg-green-50 text-green-800 dark:border-green-700 dark:bg-green-900/30 dark:text-green-300'
							: isCorrect === false
								? 'border-red-300 bg-red-50 text-red-800 dark:border-red-700 dark:bg-red-900/30 dark:text-red-300'
								: 'border-gray-300 bg-gray-50 text-gray-800 dark:border-gray-700 dark:bg-gray-900/30 dark:text-gray-300'
					}`}
				>
					{#if isCorrect === true}
						<div class="flex items-center font-medium">
							<Check class="mr-2 h-5 w-5" />
							{$_('block.correctAnswer')}
						</div>
					{:else if isCorrect === false}
						<div class="flex items-center font-medium">
							<X class="mr-2 h-5 w-5" />
							{$_('block.incorrectAnswer')}
						</div>
						{#if currentExpectedAnswer.trim() !== ''}
							<p class="mt-1 text-sm">
								{$_('block.expectedAnswerWas')}:
								<code class="font-mono">{currentExpectedAnswer}</code>
							</p>
						{/if}
					{:else}
						<div class="flex items-center font-medium">
							{$_('block.feedbackUnavailable')}
						</div>
					{/if}
				</div>
			{/if}
		</div>
	{/snippet}
</EditPreviewTabContainer>
