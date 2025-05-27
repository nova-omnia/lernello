<script lang="ts">
	import { _ } from 'svelte-i18n';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import LearningUnitTrainingContainer from '$lib/components/LearningUnitTrainer.svelte';
	import { blockActionState } from '$lib/states/blockActionState.svelte.js';
	import { learningUnitProgressState } from '$lib/states/LearningUnitProgressState.svelte';
	import { INSTRUCTOR_ROLE, TRAINEE_ROLE, type UserInfo } from '$lib/schemas/response/UserInfo.js';
	import { toaster } from '$lib/states/toasterState.svelte.js';
	import type { LearningUnitProgressRes } from '$lib/schemas/response/progress/LearningUnitProgressResSchema';
	import type { LearningUnitRes } from '$lib/schemas/response/LearningUnitRes';
	import ToggleViewButton from '$lib/components/ToggleViewButton.svelte';

	interface PageDataFromServer {
		learningUnitId: string;
		learningUnit: LearningUnitRes;
		initialLearningUnitProgress: LearningUnitProgressRes | null;
		userInfo?: UserInfo | null;
	}

	let { data }: { data: PageDataFromServer } = $props();

	const role = TRAINEE_ROLE; // the role is always TRAINEE_ROLE for this page

	blockActionState.setBlocks(data.learningUnit.blocks);

	$effect(() => {
		if (data.initialLearningUnitProgress) {
			learningUnitProgressState.setProgress(data.initialLearningUnitProgress);
		} else {
			if (data.userInfo && data.userInfo.role === TRAINEE_ROLE && data.learningUnitId) {
				toaster.create({
					description: $_('learningUnit.error.loadProgressDescription'),
					type: 'error'
				});
			}
		}

		return () => {
			learningUnitProgressState.clearProgress();
		};
	});
</script>

<PageContainer
	title={$_('learningUnit.details', {
		values: { name: data.learningUnit.name }
	})}
>
	{#if data.userInfo?.role === INSTRUCTOR_ROLE}
		<ToggleViewButton />
	{/if}
	<LearningUnitTrainingContainer {role} />
</PageContainer>
