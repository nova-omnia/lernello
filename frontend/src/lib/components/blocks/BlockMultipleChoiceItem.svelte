<script lang="ts">
	import { Switch } from '@skeletonlabs/skeleton-svelte';
	import { Check, X, WandSparkles } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import AddAITheoryBlockModal from './../AddAITheoryBlockModal.svelte';

	const { block } = $props();

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
	let showAddTraineeModal = $state(false);

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
		console.log({
			question,
			possibleAnswers: newPossibleAnswers,
			correctAnswers: newCorrectAnswers
		});
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
</script>

<div class="rounded-lg bg-white p-4 dark:bg-gray-800">
	<input
		type="text"
		placeholder={$_('common.block.question')}
		bind:value={question}
		class="input col-span-12 mb-4 w-full border p-2"
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
				class="input col-span-10 p-2"
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

	<div class="flex gap-2">
		<button onclick={addAnswerField} type="button" class="btn preset-filled-primary-500">
			{$_('block.multipleChoiceBlocks.addButton')}
		</button>
		<button class="btn btn-primary btn-sm" onclick={() => (showAddTraineeModal = true)}>
			<WandSparkles />
		</button>
	</div>
</div>

<AddAITheoryBlockModal bind:isOpen={showAddTraineeModal} blockId={block.uuid} />
