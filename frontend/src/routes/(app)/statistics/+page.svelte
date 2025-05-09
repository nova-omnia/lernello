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

	async function fetchAllKits(): Promise<LearningKitRes[]> {
		const allKits = await api(fetch).req(getLearningKits, null, { size: 100, page: 0 }).parse();
		return allKits.content;
	}
</script>

<PageContainer title={$_('statistics.overview.title')}>
	<LearningKitsStatisticsOverview fetchKits={fetchAllKits} />
</PageContainer>
