<script lang="ts">
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import { _ } from 'svelte-i18n';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';
	import { goto } from '$app/navigation';
	import LearningKitStatistic from '$lib/components/statistics/LearningKitStatistic.svelte';
	import { getLearningKitById } from '$lib/api/collections/learningKit';
	import { createQuery } from '@tanstack/svelte-query';
	import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';
	import { api } from '$lib/api/apiClient';
	import {SquareArrowOutUpRight} from "lucide-svelte";

	const { data: pageData } = $props();

	if (pageData.role !== INSTRUCTOR_ROLE) {
		goto('/dashboard');
	}

	const learningKitId = pageData.learningkitId;

	const learningKitQuery = createQuery<LearningKitRes>({
		queryKey: ['learning-kit', learningKitId],
		queryFn: () => api(fetch).req(getLearningKitById, null, learningKitId).parse()
	});

	function navigateToLearningKit() {
		goto(`/learningkit/${learningKitId}`);
	}
</script>

<PageContainer>
	<div class="flex w-full justify-between gap-4">

	<h1 class="preset-typo-headline">
		{ $_('statistics.title.withName', {values: { name: $learningKitQuery.data?.name ?? $_('statistics.loadingTitle') }}) }
	</h1>
	<div class="mb-4 flex justify-end h-full">
		<button
			type="button"
			class="btn preset-filled-primary-500 h-full"
			onclick={navigateToLearningKit}
		>
			{$_('actions.open')}
			{$learningKitQuery.data?.name ?? $_('learningKit.title')}
			<SquareArrowOutUpRight />
		</button>
	</div>
	</div>
	<LearningKitStatistic {learningKitId} />
</PageContainer>
