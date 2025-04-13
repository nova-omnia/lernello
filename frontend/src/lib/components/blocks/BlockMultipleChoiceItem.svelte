<script lang="ts">
	import { Switch } from '@skeletonlabs/skeleton-svelte';
	import { Check, X } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';

	let question = '';
	let answers: { value: string; isCorrect: boolean }[] = [
		{ value: '', isCorrect: false },
		{ value: '', isCorrect: false }
	];

	function addAnswerField() {
		answers = [...answers, { value: '', isCorrect: false }];
	}

	function toggleCorrect(index: number) {
		if (index >= 0 && index < answers.length) {
			answers = answers.map((answer, i) =>
				i === index ? { ...answer, isCorrect: !answer.isCorrect } : answer
			);
		}
	}
</script>

<div class="rounded border border-green-300 bg-green-100 p-4 shadow">
	<h2 class="text-xl font-bold text-green-800">{$_('block.multipleChoiceQuiz')}</h2>
	<input
		type="text"
		placeholder={$_('block.multipleChoiceBBlocks.question')}
		bind:value={question}
		class="input col-span-12 mb-4 w-full border p-2"
	/>

	{#each answers as answer, idx (idx)}
		<div class="col-span-12 mb-2 grid grid-cols-12 items-center gap-4">
			<Switch
				name="correct-{idx}"
				controlActive="bg-primary-400"
				classes="col-span-1"
				onCheckedChange={() => toggleCorrect(idx)}
			>
				{#snippet inactiveChild()}<X size="14" />{/snippet}
				{#snippet activeChild()}<Check size="14" />{/snippet}
			</Switch>
			<input
				type="text"
				placeholder={`${$_('block.multipleChoiceBlocks.answer')} ${idx + 1}`}
				class="input col-span-11 p-2"
				bind:value={answer.value}
			/>
		</div>
	{/each}
	<button on:click={addAnswerField} class="bg-primary-100-900 col-span-12 mt-4 rounded p-4">
		{$_('block.multipleChoiceBBlocks.addButton')}
	</button>
</div>
