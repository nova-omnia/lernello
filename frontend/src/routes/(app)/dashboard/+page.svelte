<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { ChevronRight } from 'lucide-svelte';
	import { getAllLearningKits } from '$lib/api/collections/learningKit';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient.js';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import AddLearningKit from '$lib/components/learningkit/AddLearningKit.svelte';
	import LearningKit from '$lib/components/learningkit/LearningKit.svelte';
	import PlaceholderLearningKit from '$lib/components/learningkit/PlaceholderLearningKit.svelte';
	import DashboardBase from '$lib/components/DashboardBase.svelte';

	const kitsQuery = createQuery({
		queryKey: ['learning-kits-list'],
		queryFn: () => api(fetch).req(getAllLearningKits, null).parse()
	});
</script>

<DashboardBase title={$_('dashboard.title')}>
	<div class="container flex space-y-4">
		<div class="container flex-col space-y-2">
			<a href="/learningkits" class="preset-typo-subtitle-navigation flex w-fit items-center">
				<h2>{$_('dashboard.manageLearningKits')}</h2>
				<ChevronRight size={24} />
			</a>
			<div class="container flex h-36 flex-wrap gap-4">
				{#if $kitsQuery.status === 'pending'}
					{#each Array(3)}
						<PlaceholderLearningKit />
					{/each}
				{:else if $kitsQuery.status === 'error'}
					<ErrorIllustration>{$_('learningKit.error.loadList')}</ErrorIllustration>
				{:else}
					{#each $kitsQuery.data as kit (kit.uuid)}
						<LearningKit title={kit.name} uuid={kit.uuid} />
					{/each}
					<AddLearningKit title={$_('learningKit.create')} />
				{/if}
			</div>
		</div>
	</div>
</DashboardBase>
