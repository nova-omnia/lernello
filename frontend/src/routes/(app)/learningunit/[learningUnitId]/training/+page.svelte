<script>
	import { _ } from 'svelte-i18n';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import LearningUnitTrainingContainer from '$lib/components/LearningUnitTrainer.svelte';
	import { blockActionState } from '$lib/states/blockActionState.svelte.js';

	let { data } = $props();

	blockActionState.setBlocks(data.learningUnit.blocks);
</script>

<PageContainer title={$_('learningUnit.trainingTitle')}>
	<LearningUnitTrainingContainer role={data.role} />
</PageContainer>
