<script lang="ts">
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import { _ } from 'svelte-i18n';
	import LearningKitsStatisticsOverview from '$lib/components/statistics/LearningKitStatisticsOverview.svelte';
	import { error } from '@sveltejs/kit';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';

	const { data } = $props();
</script>

{#if data.userInfo.role === INSTRUCTOR_ROLE}
	<PageContainer title={$_('statistics.overview.title')}>
		<div class="flex w-full justify-between">
			<p>{$_('statistics.instructor.noKits.description')}</p>
		</div>

		<LearningKitsStatisticsOverview maxKitsToShow={100} />
	</PageContainer>
{:else}
	{error(403, 'You are not allowed to view this page')}
{/if}
