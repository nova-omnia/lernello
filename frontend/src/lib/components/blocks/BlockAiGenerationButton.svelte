<script lang="ts">
	import { THEORY_BLOCK_TYPE, type BlockRes } from '$lib/schemas/response/BlockRes';
	import GenerateQuizModal from '$lib/components/dialogs/GenerateQuizModal.svelte';
	import GenerateTheoryModal from '$lib/components/dialogs/GenerateTheoryModal.svelte';
	import { _ } from 'svelte-i18n';
	import { Sparkles } from 'lucide-svelte';
	import { createMutation } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import {
		generateAITheoryBlock,
		generatedAIMultipleChoiceBlock,
		generatedAIQuestionBlock
	} from '$lib/api/collections/aiBlock';
	import type { GeneratedAIQuestionBlock } from '$lib/schemas/request/block/GeneratedAIQuestionBlock';
	import type { GenerateAITheoryBlock } from '$lib/schemas/request/block/GenerateAITheoryBlock';
	import { blockActionState, queueBlockAction } from '$lib/states/blockActionState.svelte';

	interface BlockAiGenerationButtonProps {
		block: BlockRes;
	}

	const { block }: BlockAiGenerationButtonProps = $props();

	let theoryBlocks: BlockRes[] = $derived(
		blockActionState.blocks.filter((b) => b.type === THEORY_BLOCK_TYPE)
	);

	let showCreationDialog = $state(false);

	const generateMultipleChoiceMutation = createMutation({
		onSuccess: (data) => {
			showCreationDialog = false;
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				question: data.question,
				possibleAnswers: data.possibleAnswers,
				correctAnswers: data.correctAnswers
			});
		},
		onError(error, variables, context) {
			console.error('Error generating multiple choice block:', error, variables, context);
		},
		mutationFn: (payload: GeneratedAIQuestionBlock) =>
			api(fetch).req(generatedAIMultipleChoiceBlock, payload).parse()
	});

	const generateQuestionMutation = createMutation({
		onSuccess: (data) => {
			showCreationDialog = false;
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				question: data.question,
				expectedAnswer: data.expectedAnswer
			});
		},
		mutationFn: (payload: GeneratedAIQuestionBlock) =>
			api(fetch).req(generatedAIQuestionBlock, payload).parse()
	});

	const generateTheoryMutation = createMutation({
		onSuccess: (data) => {
			showCreationDialog = false;
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				content: data.content
			});
		},
		mutationFn: (payload: GenerateAITheoryBlock) =>
			api(fetch).req(generateAITheoryBlock, payload).parse()
	});
</script>

<button
	type="button"
	class="btn preset-filled-primary-500"
	title={$_('block.generateAi.description')}
	onclick={(e) => {
		e.preventDefault();
		showCreationDialog = true;
	}}
>
	{$_('block.generateAi')}
	<Sparkles size={18} />
</button>

{#if block.type === 'MULTIPLE_CHOICE'}
	<GenerateQuizModal
		isLoading={$generateMultipleChoiceMutation.isPending}
		isOpen={showCreationDialog}
		onConfirm={(selectedBlockId) => {
			$generateMultipleChoiceMutation.mutate({
				blockId: block.uuid,
				theoryBlockId: selectedBlockId
			});
		}}
		onCancel={() => (showCreationDialog = false)}
		theoryBlocks={theoryBlocks.map((theoryBlock) => ({
			id: theoryBlock.uuid,
			title: theoryBlock.name
		}))}
	/>
{:else if block.type === 'THEORY'}
	<GenerateTheoryModal
		isLoading={$generateTheoryMutation.isPending}
		bind:isOpen={showCreationDialog}
		onConfirm={(topic, files) => {
			$generateTheoryMutation.mutate({
				topic,
				files,
				blockId: block.uuid
			});
		}}
	/>
{:else if block.type === 'QUESTION'}
	<GenerateQuizModal
		isLoading={$generateQuestionMutation.isPending}
		isOpen={showCreationDialog}
		onConfirm={(selectedBlockId: string) => {
			$generateQuestionMutation.mutate({
				blockId: block.uuid,
				theoryBlockId: selectedBlockId
			});
		}}
		onCancel={() => (showCreationDialog = false)}
		theoryBlocks={theoryBlocks.map((theoryBlock) => ({
			id: theoryBlock.uuid,
			title: theoryBlock.name
		}))}
	/>
{/if}
