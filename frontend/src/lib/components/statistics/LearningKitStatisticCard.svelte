<script lang="ts">
	import { Progress } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';
	import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getLearningKitById } from '$lib/api/collections/learningKit';

	interface LearningKitStatisticCardProps {
		learningKitId: string;
	}

	const { learningKitId }: LearningKitStatisticCardProps = $props();

	const kitQuery = createQuery<LearningKitRes>({
		queryKey: ['learning-kit-stats-detail', learningKitId],
		queryFn: () => api(fetch).req(getLearningKitById, null, learningKitId).parse()
	});
</script>

<a
	href={`/statistics/${learningKitId}`}
	class="card preset-filled-surface-100-900 hover:preset-filled-surface-200-800 block p-4"
>
	<h3 class="preset-typo-heading mb-2 truncate">{$kitQuery.data?.name}</h3>
	<p class="text-surface-500-400 text-sm">
		{$_('statistics.numberOfParticipants', {
			values: { count: $kitQuery.data?.participants?.length ?? 0 }
		})}
	</p>

	<div class="my-2">
		<p class="text-xs">
			{$_('statistics.averageProgress')}:
			{$kitQuery.data?.averageProgress != null
				? `${$kitQuery.data?.averageProgress}%`
				: $_('statistics.overview.noData')}
		</p>
		<Progress
			value={$kitQuery.data?.averageProgress ?? 0}
			max={100}
			meterBg="bg-primary-500"
			height="h-2"
		/>
	</div>

	<div class="my-2">
		<p class="text-xs">
			{$_('statistics.completionRate')}:
			{$kitQuery.data?.completionRate != null
				? `${$kitQuery.data?.completionRate}%`
				: $_('statistics.overview.noData')}
		</p>
		<Progress
			value={$kitQuery.data?.completionRate ?? 0}
			max={100}
			meterBg="bg-success-500"
			height="h-2"
		/>
	</div>

	<p class="text-surface-400-500 mt-2 text-xs">{$_('statistics.clickForDetails')}</p>
</a>
