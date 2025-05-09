<script lang="ts">
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getLearningKits } from '$lib/api/collections/learningKit';
	import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';
	import type { LearningKitProgressRes } from '$lib/schemas/response/progress/LearningKitProgressResSchema';
	import { getLearningKitProgressForAllParticipants } from '$lib/api/collections/progress';
	import PlaceholderLearningKit from '$lib/components/learningkit/PlaceholderLearningKit.svelte';
	import LearningKitStatisticCard from '$lib/components/statistics/LearningKitStatisticCard.svelte';
	import { _ } from 'svelte-i18n';

	const kitsQuery = createQuery({
		queryKey: ['instructor-learning-kits-stats'],
		queryFn: () => api(fetch).req(getLearningKits, null, { size: 100, page: 0 }).parse() // Fetch more kits for stats
	});

	interface KitWithProgress extends LearningKitRes {
		participantProgress?: LearningKitProgressRes[];
		averageProgress?: number;
		completionRate?: number;
	}

	let kitsWithStats = $derived<KitWithProgress[]>([]);

	$effect(() => {
		if ($kitsQuery.isSuccess && $kitsQuery.data) {
			const kits = $kitsQuery.data.content;
			// Initialize kitsWithStats with base kit data and placeholders for stats
			const initialKitsWithStats = kits.map((k) => ({
				...k,
				participantProgress: undefined as LearningKitProgressRes[] | undefined,
				averageProgress: undefined as number | undefined,
				completionRate: undefined as number | undefined
			}));
			kitsWithStats = initialKitsWithStats;

			initialKitsWithStats.forEach(async (kit) => {
				try {
					const progressData = await api(fetch)
						.req(getLearningKitProgressForAllParticipants, null, kit.uuid)
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

					kitsWithStats = kitsWithStats.map((k) =>
						k.uuid === kit.uuid
							? {
									...k,
									participantProgress: progressData || [],
									averageProgress: avgProgress,
									completionRate: compRate
								}
							: k
					);
				} catch (error) {
					console.error(`Failed to load progress for kit ${kit.name}:`, error);
					kitsWithStats = kitsWithStats.map((k) =>
						k.uuid === kit.uuid
							? { ...k, participantProgress: [], averageProgress: 0, completionRate: 0 } // Handle error case
							: k
					);
				}
			});
		} else if (!$kitsQuery.isPending) {
			kitsWithStats = [];
		}
	});
</script>

{#if $kitsQuery.isPending}
	<div class="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5">
		{#each Array(3) as _}
			<PlaceholderLearningKit />
		{/each}
	</div>
{:else if $kitsQuery.isError}
	<ErrorIllustration>{$_('learningKit.error.loadList')}</ErrorIllustration>
{:else if kitsWithStats.length === 0 && $kitsQuery.isSuccess}
	<div class="text-center">
		<p>{$_('statistics.instructor.noKits')}</p>
	</div>
{:else}
	<div class="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5">
		{#each kitsWithStats as kit (kit.uuid)}
			<LearningKitStatisticCard {kit} />
		{/each}
	</div>
{/if}
