<script lang="ts">
	import { api } from '$lib/api/apiClient';
	import { getLearningKitProgressForAllParticipants } from '$lib/api/collections/progress';
	import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';
	import type { KitWithProgress } from '$lib/types/statistics';
	import LearningKitStatisticCard from './LearningKitStatisticCard.svelte';
	import PlaceholderLearningKit from '$lib/components/learningkit/PlaceholderLearningKit.svelte';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import { _ } from 'svelte-i18n';
	import { createQuery } from '@tanstack/svelte-query';

	interface Props {
		kits: LearningKitRes[];
		maxKitsToShow?: number;
	}

	const { kits, maxKitsToShow = 5 }: Props = $props();

	const kitsStatisticsQuery = createQuery<KitWithProgress[]>({
		queryKey: ['kits-statistics-overview', kits.map((k) => k.uuid).sort().join(',')],
		queryFn: async () => {
			if (!kits || kits.length === 0) {
				return [];
			}

			const processedKits: KitWithProgress[] = [];

			for (const kitRes of kits) {
				try {
					const progressData = await api(fetch)
							.req(getLearningKitProgressForAllParticipants, null, kitRes.uuid)
							.parse();

					let totalProgress = 0;
					let completedCount = 0;
					let avgProgress = 0;
					let compRate = 0;

					if (progressData && progressData.length > 0) {
						progressData.forEach((p) => {
							totalProgress += p.progressPercentage;
							if (p.isCompleted) {
								completedCount++;
							}
						});
						avgProgress = totalProgress / progressData.length;
						compRate = (completedCount / progressData.length) * 100;
					}
					processedKits.push({
						...kitRes,
						participantProgress: progressData || [],
						averageProgress: avgProgress,
						completionRate: compRate
					} as KitWithProgress);
				} catch (e) {
					console.error(`Failed to load progress for kit ${kitRes.name}:`, e);
					processedKits.push({
						...kitRes,
						participantProgress: [],
						averageProgress: 0,
						completionRate: 0
					} as KitWithProgress);
				}
			}
			return processedKits;
		}
	});
</script>

{#if $kitsStatisticsQuery.isLoading}
	<div class="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5">
		{#each Array(kits.length || maxKitsToShow)}
			<PlaceholderLearningKit />
		{/each}
	</div>
{:else if $kitsStatisticsQuery.isError}
	<ErrorIllustration>{$_('learningKit.error.loadList')}</ErrorIllustration>
{:else if !$kitsStatisticsQuery.data || $kitsStatisticsQuery.data.length === 0}
	<div class="text-center">
		<p>{$_('statistics.instructor.noKits')}</p>
	</div>
{:else}
	<div class="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5">
		{#each $kitsStatisticsQuery.data as kit (kit.uuid)}
			<LearningKitStatisticCard {kit} />
		{/each}
	</div>
{/if}