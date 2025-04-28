<script lang="ts">
	import { Switch } from '@skeletonlabs/skeleton-svelte';
	import { Check, X } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import AddAITheoryBlockModal from './../AddAITheoryBlockModal.svelte';
	import { type BlockRes, MULTIPLE_CHOICE_BLOCK_TYPE } from '$lib/schemas/response/BlockRes';
	import { INSTRUCTOR_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';

	const Tab = {
		EDIT: 'edit',
		PREVIEW: 'preview'
	} as const;
	type Tab = (typeof Tab)[keyof typeof Tab];
	let activeTab: Tab = $state(Tab.EDIT);

	interface BlockMultipleChoiceItemProps {
		block: Extract<BlockRes, { type: typeof MULTIPLE_CHOICE_BLOCK_TYPE }>;
		role: RoleType;
	}

	const { block, role }: BlockMultipleChoiceItemProps = $props();

	type Answer = { value: string; isCorrect: boolean };

	let question = $state(block.question);
	let answers = $state<Answer[]>(
		block.possibleAnswers.map(
			(answer: string): Answer => ({
				value: answer,
				isCorrect: block.correctAnswers.includes(answer)
			})
		)
	);

	let lastQuestion = $state(block.question);
	let lastPossibleAnswers = $state([...block.possibleAnswers]);
	let lastCorrectAnswers = $state([...block.correctAnswers]);

	function handleUpdate() {
		const newPossibleAnswers = answers.map((a: Answer) => a.value);
		const newCorrectAnswers = answers
			.filter((answer: Answer) => answer.isCorrect)
			.map((answer: Answer) => answer.value);
		if (
			question !== lastQuestion ||
			JSON.stringify(newPossibleAnswers) !== JSON.stringify(lastPossibleAnswers) ||
			JSON.stringify(newCorrectAnswers) !== JSON.stringify(lastCorrectAnswers)
		) {
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				question,
				possibleAnswers: newPossibleAnswers,
				correctAnswers: newCorrectAnswers
			});

			lastQuestion = question;
			lastPossibleAnswers = newPossibleAnswers;
			lastCorrectAnswers = newCorrectAnswers;
		}
	}

	function addAnswerField() {
		answers = [...answers, { value: '', isCorrect: false }];
	}

	function toggleCorrect(index: number) {
		answers = answers.map((answer: Answer, i: number) =>
			i === index ? { ...answer, isCorrect: !answer.isCorrect } : answer
		);
	}

	function updateAnswer(index: number, value: string) {
		answers = answers.map((answer: Answer, i: number) =>
			i === index ? { ...answer, value } : answer
		);
		handleUpdate();
	}

	function removeAnswer(index: number) {
		if (answers.length > 1) {
			answers = answers.filter((_, i: number) => i !== index);
		}
	}

	$effect(() => {
		handleUpdate();
	});

	//TODO
	let selectedAnswer = $state<number | null>(null);
	let isSubmitted = $state(false);

	function handleSubmit() {
		isSubmitted = true;
	}
</script>

<div class="rounded-lg bg-white p-4 dark:bg-gray-800">
	{#if role === INSTRUCTOR_ROLE}
		<div class="mb-4 flex border-b dark:border-gray-700">
			<button
				class={`rounded-none px-4 py-2 text-sm font-medium ${
					activeTab === Tab.EDIT
						? 'border-primary-500 text-primary-500 border-b-2'
						: 'text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200'
				}`}
				onclick={() => (activeTab = Tab.EDIT)}
			>
				{$_('common.edit')}
			</button>
			<button
				class={`rounded-none px-4 py-2 text-sm font-medium ${
					activeTab === Tab.PREVIEW
						? 'border-primary-500 text-primary-500 border-b-2'
						: 'text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200'
				}`}
				onclick={() => {
					activeTab = Tab.PREVIEW;
					isSubmitted = false;
					selectedAnswer = null;
				}}
			>
				{$_('common.preview')}
			</button>
		</div>
	{/if}

	{#if role === INSTRUCTOR_ROLE && activeTab === Tab.EDIT}
		<input
			type="text"
			placeholder={$_('common.block.question')}
			bind:value={question}
			class="input col-span-12 mb-4 w-full border p-2 dark:bg-gray-700"
		/>

		{#each answers as answer, idx (idx)}
			<div class="col-span-12 mb-2 grid grid-cols-12 items-center gap-4 pl-4">
				<Switch
					name={`correct-${idx}`}
					checked={answer.isCorrect}
					controlActive="bg-primary-400"
					classes="col-span-1"
					onCheckedChange={() => toggleCorrect(idx)}
				>
					{#snippet inactiveChild()}<X size="14" />{/snippet}
					{#snippet activeChild()}<Check size="14" />{/snippet}
				</Switch>
				<input
					type="text"
					placeholder={`${$_('common.block.answer')} ${idx + 1}`}
					class="input col-span-10 p-2 dark:bg-gray-700"
					value={answer.value}
					oninput={(e) => updateAnswer(idx, e.currentTarget.value)}
					onblur={() => handleUpdate()}
				/>
				<button
					onclick={() => removeAnswer(idx)}
					class="col-span-1 text-red-500 hover:text-red-700"
					title={$_('common.remove')}
					type="button"
				>
					<X size="18" />
				</button>
			</div>
		{/each}

		<div class="mt-4 flex gap-2">
			<button onclick={addAnswerField} class="px-4 py-2">
				{$_('block.multipleChoiceBlocks.addButton')}
			</button>
			<button onclick={() => (showAddTraineeModal = true)} class="px-4 py-2">
				<WandSparkles class="h-4 w-4" />
			</button>
		</div>
	{:else}
		<div class="space-y-4">
			<h3 class="text-lg font-semibold dark:text-gray-200">{question}</h3>

			<div class="space-y-2">
				{#each answers as answer, idx (idx)}
					<button
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

			{#if !isSubmitted}
				<button onclick={handleSubmit} disabled={selectedAnswer === null} class="mt-4 w-full">
					{$_('common.submit')}
				</button>
			{:else}
				<div class="mt-4 rounded-lg bg-gray-100 p-3 dark:bg-gray-700">
					{#if selectedAnswer !== null && answers[selectedAnswer]?.isCorrect}
						<span class="text-green-600 dark:text-green-400">✓ {$_('block.correctAnswer')}</span>
					{:else}
						<span class="text-red-600 dark:text-red-400">✗ {$_('block.incorrectAnswer')}</span>
					{/if}
				</div>
			{/if}
		</div>
	{/if}
</div>
