<script lang="ts">
	import { _ } from 'svelte-i18n';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import BlockIcon from './BlockIcon.svelte';
	import { WandSparkles } from 'lucide-svelte';
	import CreateMultipleChoiceModal from '../dialogs/CreateMultipleChoiceModal.svelte';
	import { api } from '$lib/api/apiClient';
	import { getLearningUnitById } from '$lib/api/collections/learningUnit';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { generateMultipleChoice } from '$lib/api/collections/aiGeneration';
	import type { AIGeneratedMultipleChoice } from '$lib/schemas/request/AIGeneratedMutipleChoice';

	interface BlockIconHeaderProps {
		block: BlockRes;
		learningUnitId?: string;
	}
	const { block, learningUnitId: learningUnitId }: BlockIconHeaderProps = $props();

	const getLearningKit = createQuery({
		queryKey: ['learning-unit', learningUnitId],
		enabled: !!learningUnitId,
		queryFn: () => {
			if (learningUnitId) {
				return api(fetch).req(getLearningUnitById, null, learningUnitId).parse();
			}
		}
	});

	const generateMultipleChoiceMutation = createMutation({
		mutationFn: (payload: AIGeneratedMultipleChoice) => {
			return api(fetch).req(generateMultipleChoice, payload).parse();
		}
	});

	let theoryBlocks: BlockRes[] = $state([]);

	let blockTypeTerm = $derived.by(() => {
		switch (block.type) {
			case 'THEORY':
				return 'block.theoryBlock';
			case 'MULTIPLE_CHOICE':
				return 'block.multipleChoiceQuiz';
			case 'QUESTION':
				return 'block.questionBlock';
			default:
				return 'Unknown Block';
		}
	});

	let showCreationDialog = $state(false);

	function handleCreationDialog(selectedBlockId: string) {
		showCreationDialog = false;
		$generateMultipleChoiceMutation.mutate({
			theoryBlockId: selectedBlockId,
			multipleChoiceBlockId: block.uuid
		});
	}

	$effect(() => {
		if ($getLearningKit.isSuccess && $getLearningKit?.data) {
			theoryBlocks = $getLearningKit?.data.blocks.filter((b: BlockRes) => b.type === 'THEORY');
		}
	});
</script>

<div class="flex items-end gap-2">
	<BlockIcon iconType={block.type} />
	<h3 class="font-medium">{block.name}</h3>
	<span class="text-sm text-gray-500">({$_(blockTypeTerm)})</span>

	{#if learningUnitId}
		<div
			class="text-primary-400 hover:text-primary-500 cursor-pointer"
			title={$_('block.generateAi')}
		>
			<WandSparkles
				onclick={(e) => {
					e.preventDefault();
					showCreationDialog = true;
				}}
			/>
		</div>
	{/if}
</div>

{#if block.type === 'MULTIPLE_CHOICE'}
	<CreateMultipleChoiceModal
		isOpen={showCreationDialog}
		onConfirm={(selectedBlockId) => handleCreationDialog(selectedBlockId)}
		onCancel={() => (showCreationDialog = false)}
		theoryBlocks={theoryBlocks.map((theoryBlock) => ({
			id: theoryBlock.uuid,
			title: theoryBlock.name
		}))}
	/>
{:else if block.type === 'THEORY'}
	<!--ToDo: Add Theory Creation Modal-->
{/if}
