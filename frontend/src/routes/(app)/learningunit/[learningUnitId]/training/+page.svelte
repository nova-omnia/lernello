<script>
	import { _ } from 'svelte-i18n';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import LearningUnitTrainingContainer from '$lib/components/LearningUnitTrainer.svelte';
	import { blockActionState } from '$lib/states/blockActionState.svelte.js';
	import { TRAINEE_ROLE } from '$lib/schemas/response/UserInfo.js';

	let { data } = $props();

	const role = TRAINEE_ROLE; // the role is always TRAINEE_ROLE for this page

	blockActionState.setBlocks(data.learningUnit.blocks);
</script>

<PageContainer title={$_('learningUnit.trainingTitle')}>
	<LearningUnitTrainingContainer {role} />
</PageContainer>
