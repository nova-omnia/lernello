<script>
	import { _ } from 'svelte-i18n';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import LearningUnitTrainingContainer from '$lib/components/LearningUnitTrainer.svelte';
	import { blockActionState } from '$lib/states/blockActionState.svelte.js';
	import { TRAINEE_ROLE } from '$lib/schemas/response/UserInfo.js';
	import { api } from '$lib/api/apiClient.js';
	import { markLearningUnitOpened } from '$lib/api/collections/progress.js';
	import { toaster } from '$lib/states/toasterState.svelte.js';

	let { data } = $props();

	const role = TRAINEE_ROLE; // the role is always TRAINEE_ROLE for this page

	blockActionState.setBlocks(data.learningUnit.blocks);

	$effect.pre(() => {
		if (data && data.userInfo.role === TRAINEE_ROLE && data.learningUnitId) {
			api(fetch)
				.req(markLearningUnitOpened, { learningUnitId: data.learningUnitId })
				.parse()
				.then(() => {
					console.log('Learning unit marked as opened successfully');
				})
				.catch((err) => {
					console.error('Failed to mark learning kit as opened:', err);
					toaster.create({
						title: $_('learningUnit.error.markOpened'),
						description: $_('error.description', { values: { status: 'unknown' } }),
						type: 'error'
					});
				});
		}
	});
</script>

<PageContainer title={$_('learningUnit.trainingTitle')}>
	<LearningUnitTrainingContainer {role} />
</PageContainer>
