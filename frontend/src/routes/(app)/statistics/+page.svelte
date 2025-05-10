<script lang="ts">
	import { api } from '$lib/api/apiClient';
	import { getLearningKits } from '$lib/api/collections/learningKit';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import { _ } from 'svelte-i18n';
	import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';
	import { goto } from '$app/navigation';
	import LearningKitsStatisticsOverview from '$lib/components/statistics/LearningKitStatisticsOverview.svelte';

	const { data } = $props();

	if (data.role !== INSTRUCTOR_ROLE) {
		goto('/dashboard');
	}
</script>

<PageContainer title={$_('statistics.overview.title')}>
	<LearningKitsStatisticsOverview maxKitsToShow={100} />
</PageContainer>
