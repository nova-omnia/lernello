<script lang="ts">
	import { api } from '$lib/api/apiClient';
	import LearningKitStatisticCard from './LearningKitStatisticCard.svelte';
	import PlaceholderLearningKit from '$lib/components/learningkit/PlaceholderLearningKit.svelte';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import { _ } from 'svelte-i18n';
	import { createQuery } from '@tanstack/svelte-query';
	import { getLearningKits } from '$lib/api/collections/learningKit';
	import type { LearningKitPage } from '$lib/schemas/response/LearningKitRes';

	interface Props {
		maxKitsToShow?: number;
	}
	const { maxKitsToShow = 5 }: Props = $props();

	const kitsQuery = createQuery<LearningKitPage>({
		queryKey: ['latest-learning-kits-list', null],
		queryFn: () => api(fetch).req(getLearningKits, null, {}).parse()
	});

	let publishedKits = $derived($kitsQuery.data?.content?.filter((kit) => kit.published) ?? []);
</script>

{#if $kitsQuery.isLoading}
	<div class="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5">
		{#each Array(maxKitsToShow)}
			<PlaceholderLearningKit />
		{/each}
	</div>
{:else if $kitsQuery.isError}
	<ErrorIllustration>{$_('learningKit.error.loadList')}</ErrorIllustration>
{:else if !$kitsQuery.data?.content || publishedKits.length === 0}
	<div class="text-center">
		<br />
		<p>{$_('statistics.instructor.noKits')}</p>
		<p>{$_('statistics.instructor.noKits.description')}</p>
	</div>
{:else}
	<div class="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5">
		{#each publishedKits.slice(0, maxKitsToShow) as kit (kit.uuid)}
			<LearningKitStatisticCard learningKitId={kit.uuid} />
		{/each}
	</div>
{/if}
