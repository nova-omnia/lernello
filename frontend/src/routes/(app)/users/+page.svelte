<script lang="ts">
	import { _ } from 'svelte-i18n';
	import PageContainer from '$lib/components/PageContainer.svelte';
	import { Tabs } from '@skeletonlabs/skeleton-svelte';
	import { User } from 'lucide-svelte';
	import TraineesOverviewTab from '$lib/components/users/TraineesOverviewTab.svelte';
	import InstructorsOverviewTab from '$lib/components/users/InstructorsOverviewTab.svelte';
	import { goto } from '$app/navigation';

	const tabGroup = {
		instructors: 'instructors',
		trainees: 'trainees'
	};

	const { data } = $props();
	let group = $state(data.tab || tabGroup.instructors);

	function handleTabChange(e: { value: string }) {
		group = e.value;
		goto(`/users?tab=${e.value}`);
	}
</script>

<!-- 	TODO View only visible for instructors -->
<PageContainer title={$_('common.users')}>
	<Tabs value={group || tabGroup.instructors} onValueChange={(e) => handleTabChange(e)}>
		{#snippet list()}
			<Tabs.Control value={tabGroup.instructors}>
				{#snippet lead()}
					<User size={20} />
				{/snippet}
				{$_('common.instructors')}
			</Tabs.Control>
			<Tabs.Control value={tabGroup.trainees}>
				{#snippet lead()}
					<User size={20} />
				{/snippet}
				{$_('common.trainees')}
			</Tabs.Control>
		{/snippet}
		{#snippet content()}
			<Tabs.Panel value={tabGroup.instructors}>
				<InstructorsOverviewTab></InstructorsOverviewTab>
			</Tabs.Panel>
			<Tabs.Panel value={tabGroup.trainees}>
				<TraineesOverviewTab></TraineesOverviewTab>
			</Tabs.Panel>
		{/snippet}
	</Tabs>
</PageContainer>
