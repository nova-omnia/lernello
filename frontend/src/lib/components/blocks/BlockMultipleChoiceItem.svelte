<script lang="ts">
	import { Switch } from '@skeletonlabs/skeleton-svelte';
	import { Check, X } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';

	interface BlockMultipleChoiceItemProps {
		blockState: BlockRes;
	}

	const { blockState }: BlockMultipleChoiceItemProps = $props();

	let question = $state(
		blockState.type === 'MULTIPLE_CHOICE' ? blockState.question : ''
	);

	let answers = $state<{ value: string; isCorrect: boolean }[]>(
		blockState.type === 'MULTIPLE_CHOICE' && Array.isArray(blockState.possibleAnswers)
			? blockState.possibleAnswers.map((a) =>
				typeof a === 'string'
					? { value: a, isCorrect: false }
					: a
			)
			: [
				{ value: '', isCorrect: false },
				{ value: '', isCorrect: false }
			]
	);

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

<div class="rounded-lg bg-white p-4 dark:bg-gray-800">
	<input
		type="text"
		placeholder={$_('block.multipleChoiceBlocks.question')}
		bind:value={question}
		class="input col-span-12 mb-4 w-full border p-2"
	/>

	{#each answers as answer, idx (idx)}
		<div class="col-span-12 mb-2 grid grid-cols-12 items-center gap-4 pl-4">
			<Switch
				name={`correct-${idx}`}
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
	<button onclick={addAnswerField} type="button" class="btn preset-filled-primary-500">
		{$_('block.multipleChoiceBlocks.addButton')}
	</button>
</div>
