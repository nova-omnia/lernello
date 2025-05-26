<script lang="ts">
	import { THEORY_BLOCK_TYPE, type BlockRes } from '$lib/schemas/response/BlockRes';
	import GenerateQuizModal from '$lib/components/dialogs/GenerateQuizModal.svelte';
	import GenerateTheoryModal from '$lib/components/dialogs/GenerateTheoryModal.svelte';
	import { _, isLoading } from 'svelte-i18n';
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
	import { toaster } from '$lib/states/toasterState.svelte';

	interface BlockAiGenerationButtonProps {
		block: BlockRes;
		isGenerationLoading: (isLoading: boolean) => void; 
	}

	const { block, isGenerationLoading }: BlockAiGenerationButtonProps = $props();

	let theoryBlocks: BlockRes[] = $derived(
		blockActionState.blocks.filter((b) => b.type === THEORY_BLOCK_TYPE)
	);

	let showCreationDialog = $state(false);

	const generateMultipleChoiceMutation = createMutation({
		onSuccess: (data) => {
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				question: data.question,
				possibleAnswers: data.possibleAnswers,
				correctAnswers: data.correctAnswers
			});
			toaster.create({
				description: $_('multipleChoiceBlock.success.description'),
				type: 'success'
			});
			isGenerationLoading(false);
		},
		onError(error, variables, context) {
			console.error('Error generating multiple choice block:', error, variables, context);
			isGenerationLoading(false);
		},
		onMutate: () => {
			toaster.create({
				description: $_('multipleChoiceBlock.loading.description'),
				type: 'loading'
			});
			isGenerationLoading(true);
			showCreationDialog = false;
		},
		mutationFn: (payload: GeneratedAIQuestionBlock) =>
			api(fetch).req(generatedAIMultipleChoiceBlock, payload).parse()
	});

	const generateQuestionMutation = createMutation({
		onSuccess: (data) => {
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				question: data.question,
				expectedAnswer: data.expectedAnswer
			});
			toaster.create({
				description: $_('questionBlock.success.description'),
				type: 'success'
			});
			isGenerationLoading(false);
		},
		onError(error, variables, context) {
			console.error('Error generating question block:', error, variables, context);
			isGenerationLoading(false);
		},
		onMutate: () => {
			toaster.create({
				description: $_('questionBlock.loading.description'),
				type: 'loading'
			});
			isGenerationLoading(true);
			showCreationDialog = false;
		},
		mutationFn: (payload: GeneratedAIQuestionBlock) =>
			api(fetch).req(generatedAIQuestionBlock, payload).parse()
	});

	const generateTheoryMutation = createMutation({
		onSuccess: (data) => {
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				content: data.content
			});
			toaster.create({
				description: $_('theoryBlock.success.description'),
				type: 'success'
			});
			isGenerationLoading(false);
		},
		onError(error, variables, context) {
			console.error('Error generating theory block:', error, variables, context);
			isGenerationLoading(false);
		},
		onMutate: () => {
			toaster.create({
				description: $_('theoryBlock.loading.description'),
				type: 'loading'
			});
			isGenerationLoading(true);
			showCreationDialog = false;
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
