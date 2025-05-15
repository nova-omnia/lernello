<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { Tabs } from '@skeletonlabs/skeleton-svelte';
	import { UserPlus, User } from 'lucide-svelte';
	import TraineesOverviewTab from '$lib/components/users/TraineesOverviewTab.svelte';
	import InstructorsOverviewTab from '$lib/components/users/InstructorsOverviewTab.svelte';
	import { goto } from '$app/navigation';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';
	import { error } from '@sveltejs/kit';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';

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

{#if data.userInfo.role === INSTRUCTOR_ROLE}
	<PageContainer>
		<div class="flex flex-col gap-8">
			<div class="flex-col">
				<div class="flex w-full justify-between gap-4">
					<h1 class="preset-typo-headline">
						{$_('common.users')}
					</h1>
					<div class="flex h-10 gap-8">
						<a
							href="/users/create-form"
							class="btn preset-filled-primary-500 flex h-full items-center gap-2"
						>
							<UserPlus size={24} />
							{$_('user.create')}
						</a>
					</div>
				</div>
				<div class="flex w-full justify-between mt-4">
					<p>{$_('statistics.instructor.noKits.description')}</p>
				</div>
			</div>
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
		</div>
	</PageContainer>
{:else}
	{error(403, 'You are not allowed to view this page')}
{/if}
