<script lang="ts">
	import { _ } from 'svelte-i18n';
	import {
		type BlockRes,
		MULTIPLE_CHOICE_BLOCK_TYPE,
		QUESTION_BLOCK_TYPE,
		THEORY_BLOCK_TYPE
	} from '$lib/schemas/response/BlockRes';
	import BlockIcon from './BlockIcon.svelte';
	import { WandSparkles } from 'lucide-svelte';
	import CreateMultipleChoiceModal from '../dialogs/CreateMultipleChoiceModal.svelte';
	import { api } from '$lib/api/apiClient';
	import { getLearningUnitById } from '$lib/api/collections/learningUnit';
	import { createQuery } from '@tanstack/svelte-query';
	import { INSTRUCTOR_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';
	import GenerateTheoryModal from '../GenerateTheoryModal.svelte';

	interface BlockIconHeaderProps {
		block: BlockRes;
		learningUnitId?: string;
		role: RoleType;
	}
	const { block, learningUnitId: learningUnitId, role }: BlockIconHeaderProps = $props();

	const getLearningKit = createQuery({
		queryKey: ['learning-unit', learningUnitId],
		enabled: !!learningUnitId,
		queryFn: () => {
			if (learningUnitId) {
				return api(fetch).req(getLearningUnitById, null, learningUnitId).parse();
			}
		}
	});

	let theoryBlocks: BlockRes[] = $state([]);

	let blockTypeTerm = $derived.by(() => {
		switch (block.type) {
			case THEORY_BLOCK_TYPE:
				return 'block.theoryBlock';
			case MULTIPLE_CHOICE_BLOCK_TYPE:
				return 'block.multipleChoiceQuiz';
			case QUESTION_BLOCK_TYPE:
				return 'block.questionBlock';
			default:
				return 'Unknown Block';
		}
	});

	let showCreationDialog = $state(false);

	function handleCreationDialog() {
		showCreationDialog = false;
	}

	$effect(() => {
		if ($getLearningKit.isSuccess && $getLearningKit?.data) {
			theoryBlocks = $getLearningKit?.data.blocks.filter(
				(b: BlockRes) => b.type === THEORY_BLOCK_TYPE
			);
		}
	});
</script>

<div class="flex w-full items-center justify-between">
	<div class="flex items-center gap-2">
		<BlockIcon iconType={block.type} />
		<div class="flex items-baseline gap-2">
			<h3 class="font-medium">{block.name}</h3>
			<span class="text-sm text-gray-500">({$_(blockTypeTerm)})</span>
		</div>
	</div>

	{#if learningUnitId && role === INSTRUCTOR_ROLE}
		<button
			type="button"
			class="text-primary-400 hover:text-primary-500 flex items-center gap-2"
			title={$_('block.generateAi')}
			onclick={(e) => {
				e.preventDefault();
				showCreationDialog = true;
			}}
		>
			<WandSparkles />
			<p class="m-0">{$_('block.generateAi')}</p>
		</button>
	{/if}
</div>

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
	<GenerateTheoryModal bind:isOpen={showCreationDialog} blockId={block.uuid} />
{:else if block.type === 'QUESTION'}
	<CreateMultipleChoiceModal
		isOpen={showCreationDialog}
		onConfirm={handleCreationDialog}
		onCancel={() => (showCreationDialog = false)}
		theoryBlocks={theoryBlocks.map((theoryBlock) => ({
			id: theoryBlock.uuid,
			title: theoryBlock.name
		}))}
	/>
{/if}
