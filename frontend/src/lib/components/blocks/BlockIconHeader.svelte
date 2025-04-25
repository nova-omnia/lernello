<script lang="ts">
	import { _ } from 'svelte-i18n';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import BlockIcon from './BlockIcon.svelte';
	import { WandSparkles } from 'lucide-svelte';
	import CreateMultipleChoiceModal from '../dialogs/CreateMultipleChoiceModal.svelte';
	import { api } from '$lib/api/apiClient';
	import { getLearningUnitById } from '$lib/api/collections/learningUnit';
	import { createQuery } from '@tanstack/svelte-query';

	interface BlockIconHeaderProps {
		block: BlockRes;
		learningUnitId: string;
	}
	const { block, learningUnitId }: BlockIconHeaderProps = $props();

	const getLearningKit = createQuery({
		queryKey: ['learning-unit', learningUnitId],
		queryFn: () => api(fetch).req(getLearningUnitById, null, learningUnitId).parse()
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

	function handleCreationDialog() {
		showCreationDialog = false;
	}

	$effect(() => {
		if ($getLearningKit?.data) {
			theoryBlocks = $getLearningKit.data.blocks.filter((b: BlockRes) => b.type === 'THEORY');
		}
	});
</script>

<div class="flex items-end gap-2">
	<BlockIcon iconType={block.type} />
	<h3 class="font-medium">{block.name}</h3>
	<span class="text-sm text-gray-500">({$_(blockTypeTerm)})</span>

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
	<!--ToDo: Add Theory Creation Modal-->
{/if}
