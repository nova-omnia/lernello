<script lang="ts">
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import { _ } from 'svelte-i18n';
	import LearningKitsStatisticsOverview from '$lib/components/statistics/LearningKitStatisticsOverview.svelte';
</script>

<PageContainer title={$_('statistics.overview.title')}>
	<div class="text-left">
		<br />
		<p>{$_('statistics.instructor.noKits')}</p>
		<p>{$_('statistics.instructor.noKits.description')}</p>
	</div>
	<LearningKitsStatisticsOverview maxKitsToShow={100} />
</PageContainer>
