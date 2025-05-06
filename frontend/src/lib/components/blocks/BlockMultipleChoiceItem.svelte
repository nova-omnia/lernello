<script lang="ts">
	import { Switch } from '@skeletonlabs/skeleton-svelte';
	import { Plus, Check, X, Trash } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import { type BlockRes, MULTIPLE_CHOICE_BLOCK_TYPE } from '$lib/schemas/response/BlockRes';
	import { type RoleType } from '$lib/schemas/response/UserInfo';
	import { createDebounced } from '$lib/utils/createDebounced';
	import EditPreviewTabContainer from '$lib/components/blocks/EditPreviewTabContainer.svelte';

	interface BlockMultipleChoiceItemProps {
		block: Extract<BlockRes, { type: typeof MULTIPLE_CHOICE_BLOCK_TYPE }>;
		role: RoleType;
	}

	const { block, role }: BlockMultipleChoiceItemProps = $props();

	type Answer = { value: string; isCorrect: boolean };

	let currentQuestion = $derived(block.question);
	let currentAnswers = $derived<Answer[]>(
		block.possibleAnswers.map(
			(answer): Answer => ({
				value: answer,
				isCorrect: block.correctAnswers.includes(answer)
			})
		)
	);

	const onUpdateHandler = createDebounced(() => {
		const newPossibleAnswers = currentAnswers.map((a: Answer) => a.value);
		const newCorrectAnswers = currentAnswers
			.filter((answer: Answer) => answer.isCorrect)
			.map((answer: Answer) => answer.value);
		if (
			currentQuestion !== block.question ||
			JSON.stringify(newPossibleAnswers) !== JSON.stringify(block.possibleAnswers) ||
			JSON.stringify(newCorrectAnswers) !== JSON.stringify(block.correctAnswers)
		) {
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				question: currentQuestion,
				possibleAnswers: newPossibleAnswers,
				correctAnswers: newCorrectAnswers
			});
		}
	}, 500);

	function addAnswerField() {
		currentAnswers = [...currentAnswers, { value: '', isCorrect: false }];
		onUpdateHandler();
	}

	function toggleCorrect(index: number) {
		currentAnswers = currentAnswers.map((answer: Answer, i: number) =>
			i === index ? { ...answer, isCorrect: !answer.isCorrect } : answer
		);
		onUpdateHandler();
	}

	function updateAnswer(index: number, value: string) {
		currentAnswers = currentAnswers.map((answer: Answer, i: number) =>
			i === index ? { ...answer, value } : answer
		);
		onUpdateHandler();
	}

	function removeAnswer(index: number) {
		if (currentAnswers.length > 1) {
			currentAnswers = currentAnswers.filter((_, i: number) => i !== index);
		}
		onUpdateHandler();
	}

	let selectedAnswer = $state<number | null>(null);
	let isSubmitted = $state(false);

	function handleSubmit() {
		isSubmitted = true;
	}
</script>

<EditPreviewTabContainer {role}>
	{#snippet editHeaderContent()}
		<div class="flex items-center space-x-2">
			<button
				type="button"
				onclick={addAnswerField}
				class="btn preset-tonal-surface"
				title={$_('block.multipleChoiceBlocks.addButton')}
			>
				<Plus class="h-5 w-5 text-gray-600 dark:text-gray-300" />
			</button>
		</div>
	{/snippet}

	{#snippet edit()}
		<input
			type="text"
			placeholder={$_('common.block.question')}
			bind:value={currentQuestion}
			onblur={onUpdateHandler}
			oninput={onUpdateHandler}
			class="input col-span-12 mb-4 w-full border p-2 dark:bg-gray-700"
		/>

		{#each currentAnswers as answer, idx (idx)}
			<div class="col-span-12 mb-2 flex items-center gap-4 pl-4">
				<Switch
					name={`correct-${idx}`}
					checked={answer.isCorrect}
					controlActive="bg-primary-400"
					onCheckedChange={() => toggleCorrect(idx)}
				>
					{#snippet inactiveChild()}<X size="14" />{/snippet}
					{#snippet activeChild()}<Check size="14" />{/snippet}
				</Switch>
				<input
					type="text"
					placeholder={`${$_('common.block.answer')} ${idx + 1}`}
					class="input h-full flex-grow p-2 dark:bg-gray-700"
					value={answer.value}
					oninput={(e) => updateAnswer(idx, e.currentTarget.value)}
					onblur={onUpdateHandler}
				/>
				<button
					onclick={() => removeAnswer(idx)}
					class="btn-icon preset-outlined-error-500 ml-auto text-red-500 hover:text-red-700"
					title={$_('common.remove')}
					type="button"
					disabled={currentAnswers.length <= 1}
				>
					<Trash size="18" />
				</button>
			</div>
		{/each}
	{/snippet}

	{#snippet preview()}
		<div class="space-y-4">
			<div class="flex items-start justify-between gap-4">
				<h3 class="text-lg font-semibold dark:text-gray-200">{currentQuestion}</h3>

				{#if !isSubmitted}
					<button
						type="button"
						onclick={handleSubmit}
						disabled={selectedAnswer === null}
						class="btn preset-filled whitespace-nowrap"
						title={$_('common.submit')}
					>
						{$_('common.submit')}
					</button>
				{:else}
					<div class="min-w-[80px] rounded-lg bg-gray-100 p-3 text-center dark:bg-gray-700">
						{#if selectedAnswer !== null && currentAnswers[selectedAnswer]?.isCorrect}
							<span class="font-medium text-green-600 dark:text-green-400">
								<Check class="mr-1 inline-block h-4 w-4" />
								{$_('block.correctAnswer')}
							</span>
						{:else}
							<span class="font-medium text-red-600 dark:text-red-400">
								<X class="mr-1 inline-block h-4 w-4" />
								{$_('block.incorrectAnswer')}
							</span>
						{/if}
					</div>
				{/if}
			</div>

			<div class="space-y-2">
				{#each currentAnswers as answer, idx (idx)}
					<button
						type="button"
						class={`w-full rounded-lg border p-3 text-left transition-colors
							${selectedAnswer === idx ? 'border-blue-300 bg-blue-100 dark:bg-blue-900/30' : 'dark:border-gray-600'}
							${isSubmitted && answer.isCorrect ? 'border-green-300 bg-green-100 dark:bg-green-900/30' : ''}
							${isSubmitted && selectedAnswer === idx && !answer.isCorrect ? 'border-red-300 bg-red-100 dark:bg-red-900/30' : ''}
							${!isSubmitted ? 'hover:bg-gray-50 dark:hover:bg-gray-700' : ''}`}
						onclick={() => !isSubmitted && (selectedAnswer = idx)}
						disabled={isSubmitted}
					>
						{answer.value}
					</button>
				{/each}
			</div>
		</div>
	{/snippet}
</EditPreviewTabContainer>
