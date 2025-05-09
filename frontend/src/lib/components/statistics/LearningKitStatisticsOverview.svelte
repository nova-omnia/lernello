<script lang="ts">
	import { api } from '$lib/api/apiClient';
	import { getLearningKitProgressForAllParticipants } from '$lib/api/collections/progress';
	import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';
	import type { KitWithProgress } from '$lib/types/statistics';
	import LearningKitStatisticCard from './LearningKitStatisticCard.svelte';
	import PlaceholderLearningKit from '$lib/components/learningkit/PlaceholderLearningKit.svelte';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import { _ } from 'svelte-i18n';

	interface Props {
		fetchKits: () => Promise<LearningKitRes[]>;
		maxKitsToShow?: number;
	}

	const { fetchKits, maxKitsToShow = 5 }: Props = $props();

	let kitsWithFullStats = $state<KitWithProgress[]>([]);
	let isLoading = $state(true);
	let isError = $state(false);
	let initialKitCount = $state(0);

	$effect(() => {
		async function loadStatistics() {
			isLoading = true;
			isError = false;
			kitsWithFullStats = [];
			let tempKitsWithStats: KitWithProgress[] = [];

			try {
				const initialKits = await fetchKits();
				initialKitCount = initialKits.length;

				if (initialKitCount === 0) {
					isLoading = false;
					return;
				}

				// Create an array of promises for fetching progress
				const progressPromises = initialKits.map(async (kitRes) => {
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
						return {
							...kitRes,
							participantProgress: progressData || [],
							averageProgress: avgProgress,
							completionRate: compRate
						} as KitWithProgress;
					} catch (e) {
						console.error(`Failed to load progress for kit ${kitRes.name}:`, e);
						// Return kit with undefined stats on error for this specific kit
						return {
							...kitRes,
							participantProgress: [],
							averageProgress: 0, // Or undefined to show placeholder
							completionRate: 0 // Or undefined
						} as KitWithProgress;
					}
				});

				tempKitsWithStats = await Promise.all(progressPromises);
				kitsWithFullStats = tempKitsWithStats;
			} catch (e) {
				console.error('Failed to fetch initial kits or their statistics:', e);
				isError = true;
			} finally {
				isLoading = false;
			}
		}
		loadStatistics();
	});
</script>

{#if isLoading}
	<div class="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5">
		{#each Array(initialKitCount || maxKitsToShow) as _}
			<PlaceholderLearningKit />
		{/each}
	</div>
{:else if isError}
	<ErrorIllustration>{$_('learningKit.error.loadList')}</ErrorIllustration>
{:else if kitsWithFullStats.length === 0}
	<div class="text-center">
		<p>{$_('statistics.instructor.noKits')}</p>
	</div>
{:else}
	<div class="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5">
		{#each kitsWithFullStats as kit (kit.uuid)}
			<LearningKitStatisticCard {kit} />
		{/each}
	</div>
{/if}
