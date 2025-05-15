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
		queryKey: ['learning-kit', learningKitId],
		queryFn: () => api(fetch).req(getLearningKitById, null, learningKitId).parse()
	});
</script>

<a
	href={`/statistics/${learningKitId}`}
	class="card preset-filled-surface-100-900 hover:preset-filled-surface-200-800 block p-4"
>
	<h3 class="mb-2 truncate font-bold">{$kitQuery.data?.name}</h3>
	<div class="flex items-center justify-between">
		<p class="text-xs font-semibold">
			{$_('statistics.numberOfTrainees')}:
		</p>
		<p class="text-xs">
			{$kitQuery.data?.trainees?.length != null
				? `${$kitQuery.data?.trainees?.length}`
				: $_('statistics.overview.noData')}
		</p>
	</div>
	<div class="my-2">
		<div class="flex items-center justify-between">
			<p class="text-xs font-semibold">
				{$_('statistics.averageProgress')}:
			</p>
			<p class="text-xs">
				{$kitQuery.data?.averageProgress != null
					? `${$kitQuery.data?.averageProgress}%`
					: $_('statistics.overview.noData')}
			</p>
		</div>
		<Progress
			value={$kitQuery.data?.averageProgress ?? 0}
			max={100}
			meterBg="bg-primary-500"
			height="h-2"
		/>
	</div>

	<div class="my-2">
		<div class="flex items-center justify-between">
			<p class="text-xs font-semibold">
				{$_('statistics.completionRate')}:
			</p>
			<p class="text-xs">
				{$kitQuery.data?.completionRate != null
					? `${$kitQuery.data?.completionRate}%`
					: $_('statistics.overview.noData')}
			</p>
		</div>
		<Progress
			value={$kitQuery.data?.completionRate ?? 0}
			max={100}
			meterBg="bg-success-500"
			height="h-2"
		/>
	</div>
</a>
