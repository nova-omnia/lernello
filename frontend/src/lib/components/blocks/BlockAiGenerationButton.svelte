<script lang="ts">
	import { type BlockRes, THEORY_BLOCK_TYPE } from '$lib/schemas/response/BlockRes';
	import CreateMultipleChoiceModal from '$lib/components/dialogs/CreateMultipleChoiceModal.svelte';
	import GenerateTheoryModal from '$lib/components/GenerateTheoryModal.svelte';
	import { _ } from 'svelte-i18n';
	import { Sparkles } from 'lucide-svelte';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getLearningUnitById } from '$lib/api/collections/learningUnit';
	import {
		generateAITheoryBlock,
		generatedAIMultipleChoiceBlock,
		generatedAIQuestionBlock
	} from '$lib/api/collections/aiBlock';
	import type { GeneratedAIQuestionBlock } from '$lib/schemas/request/block/GeneratedAIQuestionBlock';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import { blockActionState, queueBlockAction } from '$lib/states/blockActionState.svelte';
	import type { GenerateAITheoryBlock } from '$lib/schemas/request/block/GenerateAITheoryBlock';

	interface BlockAiGenerationButtonProps {
		block: BlockRes;
		learningUnitId: string;
	}

	const { block, learningUnitId: learningUnitId }: BlockAiGenerationButtonProps = $props();
	const invalidate = useQueryInvalidation();

	let showCreationDialog = $state(false);

	let theoryBlocks: BlockRes[] = $state([]);

	const getLearningKit = createQuery({
		queryKey: ['learning-unit-theory', learningUnitId],
		enabled: !!learningUnitId,
		queryFn: async () => {
			if (learningUnitId) {
				const learningUnit = await api(fetch)
					.req(getLearningUnitById, null, learningUnitId)
					.parse();
				loadTheoryBlocks();
				blockActionState.setBlocks(learningUnit.blocks ?? []);
				blockActionState.clearQueue();
				return learningUnit;
			}
		}
	});

	const generateMultipleChoiceMutation = createMutation({
		onSuccess: () => {
			blockActionState.setBlocks([]);
			blockActionState.clearQueue();
			invalidate(['learning-unit-theory']);
			showCreationDialog = false;
		},
		mutationFn: (payload: GeneratedAIQuestionBlock) => {
			return api(fetch).req(generatedAIMultipleChoiceBlock, payload).parse();
		}
	});

	const generateQuestionMutation = createMutation({
		onSuccess: () => {
			blockActionState.setBlocks([]);
			blockActionState.clearQueue();
			invalidate(['learning-unit-theory']);
			showCreationDialog = false;
		},
		mutationFn: (payload: GeneratedAIQuestionBlock) => {
			return api(fetch).req(generatedAIQuestionBlock, payload).parse();
		}
	});

	const generateTheoryMutation = createMutation({
		onSuccess: () => {
			blockActionState.setBlocks([]);
			blockActionState.clearQueue();
			invalidate(['learning-unit-theory']);
			showCreationDialog = false;
		},
		mutationFn: (payload: GenerateAITheoryBlock) => {
			return api(fetch).req(generateAITheoryBlock, payload).parse();
		}
	});

	$effect(() => {
		if ($getLearningKit.data) {
			loadTheoryBlocks();
		}
	});

	function loadTheoryBlocks() {
		if ($getLearningKit?.data) {
			theoryBlocks = $getLearningKit?.data.blocks.filter(
				(b: BlockRes) => b.type === THEORY_BLOCK_TYPE
			);
		}
	}

	function handleCreationDialog(selectedBlockId: string) {
		$generateMultipleChoiceMutation.mutate({
			theoryBlockId: selectedBlockId,
			questionBlockId: block.uuid
		});
	}

	function handleQuestionCreationDialog(selectedBlockId: string) {
		$generateQuestionMutation.mutate({
			theoryBlockId: selectedBlockId,
			questionBlockId: block.uuid
		});
	}

	function handleTheoryCreationDialog(blockId: string, topic: string, files: string[]) {
		$generateTheoryMutation.mutate({
			blockId,
			topic,
			files
		});
	}
</script>

<button
	type="button"
	class="btn preset-filled-primary-500"
	title={$_('block.generateAi.description')}
	onclick={(e) => {
		e.preventDefault();
		loadTheoryBlocks();
		showCreationDialog = true;
	}}
>
	{$_('block.generateAi')}
	<Sparkles size={18} />
</button>

{#if block.type === 'MULTIPLE_CHOICE'}
	<CreateMultipleChoiceModal
		isOpen={showCreationDialog}
		onConfirm={handleCreationDialog}
		onCancel={() => (showCreationDialog = false)}
		theoryBlocks={theoryBlocks.map((theoryBlock) => ({
			id: theoryBlock.uuid,
			title: theoryBlock.name
		}))}
	/>
{:else if block.type === 'THEORY'}
	<GenerateTheoryModal
		bind:isOpen={showCreationDialog}
		blockId={block.uuid}
		onConfirm={handleTheoryCreationDialog}
	/>
{:else if block.type === 'QUESTION'}
	<CreateMultipleChoiceModal
		isOpen={showCreationDialog}
		onConfirm={handleQuestionCreationDialog}
		onCancel={() => (showCreationDialog = false)}
		theoryBlocks={theoryBlocks.map((theoryBlock) => ({
			id: theoryBlock.uuid,
			title: theoryBlock.name
		}))}
	/>
{/if}
