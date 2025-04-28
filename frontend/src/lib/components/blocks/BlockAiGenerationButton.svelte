<script lang="ts">
	import { type BlockRes, THEORY_BLOCK_TYPE } from '$lib/schemas/response/BlockRes';
	import CreateMultipleChoiceModal from '$lib/components/dialogs/CreateMultipleChoiceModal.svelte';
	import GenerateTheoryModal from '$lib/components/GenerateTheoryModal.svelte';
	import { _ } from 'svelte-i18n';
	import { Sparkles } from 'lucide-svelte';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getLearningUnitById } from '$lib/api/collections/learningUnit';

	interface BlockAiGenerationButtonProps {
		block: BlockRes;
		learningUnitId?: string;
	}

	const { block, learningUnitId: learningUnitId }: BlockAiGenerationButtonProps = $props();

	let showCreationDialog = $state(false);

	function handleCreationDialog() {
		showCreationDialog = false;
	}

	let theoryBlocks: BlockRes[] = $state([]);

	const getLearningKit = createQuery({
		queryKey: ['learning-unit', learningUnitId],
		enabled: !!learningUnitId,
		queryFn: () => {
			if (learningUnitId) {
				return api(fetch).req(getLearningUnitById, null, learningUnitId).parse();
			}
		}
	});

	$effect(() => {
		if ($getLearningKit.isSuccess && $getLearningKit?.data) {
			theoryBlocks = $getLearningKit?.data.blocks.filter(
				(b: BlockRes) => b.type === THEORY_BLOCK_TYPE
			);
		}
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
