<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { getLearningKits } from '$lib/api/collections/learningKit';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient.js';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import AddLearningKit from '$lib/components/learningkit/AddLearningKit.svelte';
	import PlaceholderLearningKit from '$lib/components/learningkit/PlaceholderLearningKit.svelte';
	import { ChevronRight } from 'lucide-svelte';
	import PageContainer from '$lib/components/PageContainer.svelte';
	import LearningKitItem from '$lib/components/learningkit/LearningKitItem.svelte';

	const { data } = $props();
	const kitsQuery = createQuery({
		queryKey: ['latest-learning-kits-list'],
		queryFn: () => api(fetch).req(getLearningKits, null, { size: 5, page: 0 }).parse()
	});
</script>

<PageContainer title={$_('dashboard.title')}>
	<div class="container flex space-y-4">
		<div class="container flex-col space-y-2">
			<a href="/learningkits" class="preset-typo-subtitle-navigation flex w-fit items-center">
				{#if $kitsQuery.status === 'success'}
					<h2>
						{$_('dashboard.allLearningKits', {
							values: {
								count: $kitsQuery.data.numberOfElements,
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
						<LearningKitItem title={kit.name} uuid={kit.uuid} />
					{/each}
					<AddLearningKit {data} title={$_('learningKit.create')} />
				{/if}
			</div>
		</div>
	</div>
</PageContainer>
