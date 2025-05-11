<script lang="ts">
	import { Progress } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';
	import type { KitWithProgress } from '$lib/types/statistics';

	interface Props {
		kit: KitWithProgress;
	}

	const { kit }: Props = $props();
</script>

<a
	href={`/statistics/${kit.uuid}`}
	class="card preset-filled-surface-100-900 hover:preset-filled-surface-200-800 block p-4"
>
	<h3 class="preset-typo-heading mb-2 truncate">{kit.name}</h3>
	<p class="text-surface-500-400 text-sm">
		{$_('statistics.numberOfParticipants', { values: { count: kit.participants.length } })}
	</p>

	{#if kit.averageProgress !== undefined && kit.completionRate !== undefined}
		<div class="my-2">
			<p class="text-xs">
				{$_('statistics.averageProgress')}: {kit.averageProgress.toFixed(0)}%
			</p>
			<Progress value={kit.averageProgress} max={100} meterBg="bg-primary-500" height="h-2" />
		</div>
		<div class="my-2">
			<p class="text-xs">
				{$_('statistics.completionRate')}: {kit.completionRate.toFixed(0)}%
			</p>
			<Progress value={kit.completionRate} max={100} meterBg="bg-success-500" height="h-2" />
		</div>
	{:else if kit.participantProgress === undefined}
		<div class="my-2 space-y-1">
			<div class="placeholder h-2 w-3/4 animate-pulse"></div>
			<div class="placeholder h-2 w-full animate-pulse"></div>
			<div class="placeholder mt-2 h-2 w-1/2 animate-pulse"></div>
			<div class="placeholder h-2 w-full animate-pulse"></div>
		</div>
	{/if}
	<p class="text-surface-400-500 mt-2 text-xs">{$_('statistics.clickForDetails')}</p>
</a>
