<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { getLearningKits } from '$lib/api/collections/learningKit';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient.js';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import AddLearningKit from '$lib/components/learningkit/AddLearningKit.svelte';
	import PlaceholderLearningKit from '$lib/components/learningkit/PlaceholderLearningKit.svelte';
	import { ChevronRight } from 'lucide-svelte';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import LearningKitItem from '$lib/components/learningkit/LearningKitItem.svelte';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';
	import LearningKitsStatisticsOverview from '$lib/components/statistics/LearningKitStatisticsOverview.svelte';
	import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';

	const { data } = $props();

	const kitsQuery = createQuery({
		queryKey: ['latest-learning-kits-list'],
		queryFn: () => api(fetch).req(getLearningKits, null, { size: 5, page: 0 }).parse()
	});

	// Query for all kits, using prefetched data for stats overview
	const allKitsForStatsQuery = createQuery({
		queryKey: ['all-instructor-kits-for-dashboard-stats'],
		queryFn: () => api(fetch).req(getLearningKits, null, { size: 100, page: 0 }).parse(),
		enabled: data.role === INSTRUCTOR_ROLE
	});

	async function fetchTop5KitsForDashboard(): Promise<LearningKitRes[]> {
		if (data.role !== INSTRUCTOR_ROLE || !$allKitsForStatsQuery.isSuccess) {
			return [];
		}
		const allKits = $allKitsForStatsQuery.data?.content ?? [];
		if (!allKits.length) return [];

		const kitsWithDeadline = allKits.filter((k) => k.deadlineDate);
		const kitsWithoutDeadline = allKits.filter((k) => !k.deadlineDate);

		// Sort kits with deadline by deadlineDate ASC (closest first)
		kitsWithDeadline.sort((a, b) => {
			if (!a.deadlineDate) return 1;
			if (!b.deadlineDate) return -1;
			return new Date(a.deadlineDate).getTime() - new Date(b.deadlineDate).getTime();
		});

		// Kits without deadline are already sorted by createdAt DESC (newest first) by the API default
		const sortedKits = [...kitsWithDeadline, ...kitsWithoutDeadline];
		return sortedKits.slice(0, 5);
	}
</script>

<PageContainer title={$_('dashboard.title')}>
	<div class="container flex space-y-4">
		<div class="container flex-col space-y-2">
			<a href="/learningkits" class="preset-typo-subtitle-navigation flex w-fit items-center">
				{#if $kitsQuery.status === 'success'}
					<h2>
						{$_('dashboard.allLearningKits', {
							values: {
								total: $kitsQuery.data.totalElements
							}
						})}
					</h2>
				{:else}
					<h2>{$_('dashboard.allLearningKits', { values: { count: NaN, total: NaN } })}</h2>
				{/if}
				<ChevronRight size={24} />
			</a>
			<div class="container flex flex-wrap gap-2">
				{#if $kitsQuery.status === 'pending'}
					{#each Array(3)}
						<PlaceholderLearningKit />
					{/each}
				{:else if $kitsQuery.status === 'error'}
					<ErrorIllustration>{$_('learningKit.error.loadList')}</ErrorIllustration>
				{:else}
					{#each $kitsQuery.data.content as kit (kit.uuid)}
						<LearningKitItem title={kit.name} uuid={kit.uuid} role={data.role} />
					{/each}
				{/if}
				{#if data.role === INSTRUCTOR_ROLE}
					<AddLearningKit title={$_('learningKit.create')} />
				{/if}
			</div>

			{#if data.role === INSTRUCTOR_ROLE}
				<a href="/statistics" class="preset-typo-subtitle-navigation flex w-fit items-center">
					{#if $allKitsForStatsQuery.isSuccess}
						{$_('dashboard.allStatistics', {
							values: {
								total: $allKitsForStatsQuery.data.totalElements
							}
						})}
					{:else}
						{$_('dashboard.allStatistics', { values: { count: NaN } })}
					{/if}
					<ChevronRight size={24} />
				</a>
				{#if $kitsQuery.status === 'pending'}
					{#each Array(3)}
						<PlaceholderLearningKit />
					{/each}
				{:else if $kitsQuery.status === 'error'}
					<ErrorIllustration>{$_('learningKit.error.loadList')}</ErrorIllustration>
				{:else}
					<LearningKitsStatisticsOverview fetchKits={fetchTop5KitsForDashboard} />
				{/if}
			{/if}
		</div>
	</div>
</PageContainer>
