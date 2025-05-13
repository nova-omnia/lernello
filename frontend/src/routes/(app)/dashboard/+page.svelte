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
	import { type LearningKitPage } from '$lib/schemas/response/LearningKitRes';

	const { data } = $props();

	const kitsQuery = createQuery<LearningKitPage>({
		queryKey: ['latest-learning-kits-list'],
		queryFn: () => api(fetch).req(getLearningKits, null, { size: 100, page: 0 }).parse()
	});

	let publishedKits = $derived($kitsQuery.data?.content?.filter((kit) => kit.published) ?? []);
</script>

<PageContainer title={$_('dashboard.title')}>
	<div class="container flex flex-col gap-8">
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
					{#each $kitsQuery.data.content.slice(0, 5) as kit (kit.uuid)}
						<LearningKitItem
							title={kit.name}
							uuid={kit.uuid}
							role={data.userInfo.role}
							published={kit.published}
						/>
					{/each}
				{/if}
				{#if data.userInfo.role === INSTRUCTOR_ROLE}
					<AddLearningKit title={$_('learningKit.create')} />
				{/if}
			</div>
		</div>
		<div class="container flex-col space-y-2">
			{#if data.userInfo.role === INSTRUCTOR_ROLE}
				<a href="/statistics" class="preset-typo-subtitle-navigation flex w-fit items-center">
					{#if $kitsQuery.isSuccess}
						{$_('dashboard.allStatistics', {
							values: {
								total: publishedKits.length
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
					<LearningKitsStatisticsOverview maxKitsToShow={5} />
				{/if}
			{/if}
		</div>
	</div>
</PageContainer>
