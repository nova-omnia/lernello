<script lang="ts">
	import { _ } from 'svelte-i18n';
	import PageContainer from '$lib/components/PageContainer.svelte';
	import { Tabs } from '@skeletonlabs/skeleton-svelte';
	import { User } from 'lucide-svelte';
	import TraineesOverviewTab from '$lib/components/users/TraineesOverviewTab.svelte';
	import InstructorsOverviewTab from '$lib/components/users/InstructorsOverviewTab.svelte';

	const TabGroup = {
		instructors: 'instructors',
		trainees: 'trainees'
	};

	let group = $state(TabGroup.instructors);
</script>

<PageContainer title={$_('common.users')}>
	<Tabs value={group} onValueChange={(e) => (group = e.value)}>
		{#snippet list()}
			<Tabs.Control value={TabGroup.instructors}>
				{#snippet lead()}
					<User size={20} />
				{/snippet}
				{$_('common.instructors')}
			</Tabs.Control>
			<Tabs.Control value={TabGroup.trainees}>
				{#snippet lead()}
					<User size={20} />
				{/snippet}
				{$_('common.trainees')}
			</Tabs.Control>
		{/snippet}
		{#snippet content()}
			<Tabs.Panel value={TabGroup.instructors}>
				<InstructorsOverviewTab></InstructorsOverviewTab>
			</Tabs.Panel>
			<Tabs.Panel value={TabGroup.trainees}>
				<TraineesOverviewTab></TraineesOverviewTab>
			</Tabs.Panel>
		{/snippet}
	</Tabs>
</PageContainer>
