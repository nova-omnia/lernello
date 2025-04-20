<script lang="ts">
	import { _ } from 'svelte-i18n';
	import DashboardBase from '$lib/components/DashboardBase.svelte';
	import PlaceholderLearningKit from '$lib/components/learningkit/PlaceholderLearningKit.svelte';
	import AddLearningKit from '$lib/components/learningkit/AddLearningKit.svelte';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import LearningKit from '$lib/components/learningkit/LearningKit.svelte';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getAllLearningKits } from '$lib/api/collections/learningKit';

	const queryKey = 'all-learning-kits-list';
	const kitsQuery = createQuery({
		queryKey: [queryKey],
		queryFn: () => api(fetch).req(getAllLearningKits, null).parse()
	});
</script>

<DashboardBase title={$_('learningKit.title')}>
	<div class="container flex space-y-4">
		<div class="container flex h-36 flex-wrap gap-2">
			{#if $kitsQuery.status === 'pending'}
				{#each Array(3)}
					<PlaceholderLearningKit />
				{/each}
			{:else if $kitsQuery.status === 'error'}
				<ErrorIllustration>{$_('learningKit.error.loadList')}</ErrorIllustration>
			{:else}
				{#each $kitsQuery.data as kit (kit.uuid)}
					<LearningKit title={kit.name} uuid={kit.uuid} {queryKey} />
				{/each}
				<AddLearningKit title={$_('learningKit.create')} />
			{/if}
		</div>
	</div>
</DashboardBase>
