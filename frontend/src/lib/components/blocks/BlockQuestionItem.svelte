<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { Check, X } from 'lucide-svelte';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import { type BlockRes, QUESTION_BLOCK_TYPE } from '$lib/schemas/response/BlockRes';
	import { type RoleType } from '$lib/schemas/response/UserInfo';
	import { createDebounced } from '$lib/utils/createDebounced';
	import EditPreviewTabContainer from '$lib/components/blocks/EditPreviewTabContainer.svelte';

	interface BlockQuestionItemProps {
		block: Extract<BlockRes, { type: typeof QUESTION_BLOCK_TYPE }>;
		role: RoleType;
	}

	const { block, role }: BlockQuestionItemProps = $props();

	let currentQuestion = $derived(block.question);
	let currentExpectedAnswer = $derived(block.expectedAnswer);

	let traineeAnswer = $state('');
	let isSubmitted = $state(false);

	const onUpdateHandler = createDebounced(() => {
		if (currentQuestion !== block.question || currentExpectedAnswer !== block.expectedAnswer) {
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				question: currentQuestion,
				expectedAnswer: currentExpectedAnswer
			});
		}
	}, 500);

	function handleSubmit() {
		isSubmitted = true;
	}

	let isCorrect = $derived(
		isSubmitted && traineeAnswer.trim().toLowerCase() === currentExpectedAnswer.trim().toLowerCase()
	);
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
			<h3 class="text-lg font-semibold dark:text-gray-200">{currentQuestion}</h3>

			<div class="flex items-end gap-4">
				<div class="flex-grow">
					<label
						for={`student-answer-${block.uuid}`}
						class="mb-1 block text-sm font-medium text-gray-700 dark:text-gray-300"
					>
						{$_('block.yourAnswer')}
					</label>
					<input
						id={`student-answer-${block.uuid}`}
						type="text"
						placeholder={$_('block.typeYourAnswer')}
						bind:value={traineeAnswer}
						disabled={isSubmitted}
						class="input w-full border p-2 disabled:cursor-not-allowed disabled:opacity-60 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400"
					/>
				</div>
				{#if !isSubmitted}
					<button
						type="button"
						onclick={handleSubmit}
						disabled={traineeAnswer.trim() === ''}
						class="btn preset-filled self-end whitespace-nowrap disabled:cursor-not-allowed disabled:opacity-50"
						title={$_('common.submit')}
					>
						{$_('common.submit')}
					</button>
				{/if}
			</div>

			{#if isSubmitted}
				<div
					class={`mt-4 rounded-lg border p-3 ${
						isCorrect
							? 'border-green-300 bg-green-50 text-green-800 dark:border-green-700 dark:bg-green-900/30 dark:text-green-300'
							: 'border-red-300 bg-red-50 text-red-800 dark:border-red-700 dark:bg-red-900/30 dark:text-red-300'
					}`}
				>
					{#if isCorrect}
						<div class="flex items-center font-medium">
							<Check class="mr-2 h-5 w-5" />
							{$_('block.correctAnswer')}
						</div>
					{:else}
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
					{/if}
				</div>
			{/if}
		</div>
	{/snippet}
</EditPreviewTabContainer>
