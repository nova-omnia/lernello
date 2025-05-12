<script lang="ts">
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import { _ } from 'svelte-i18n';
	import LearningKitsStatisticsOverview from '$lib/components/statistics/LearningKitStatisticsOverview.svelte';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';
	import { error } from '@sveltejs/kit';

	const { data } = $props();
</script>

{#if data.userInfo.role === INSTRUCTOR_ROLE}
	<PageContainer title={$_('statistics.overview.title')}>
		<LearningKitsStatisticsOverview maxKitsToShow={100} />
	</PageContainer>
{:else}
	{error(403, 'You are not allowed to view this page')}
{/if}
