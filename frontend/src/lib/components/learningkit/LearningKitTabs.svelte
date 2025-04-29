<script lang="ts">
	import { Tabs } from '@skeletonlabs/skeleton-svelte';
	import { BookText, Files, Users } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
	import LearningUnitsTab from '$lib/components/learningkit/tab/LearningUnitsTab.svelte';
	import TraineesTab from '$lib/components/learningkit/tab/TraineesTab.svelte';
	import FilesTab from '$lib/components/learningkit/tab/FilesTab.svelte';
	import { goto } from '$app/navigation';

	const tabs = {
		learningUnits: 'learningUnits',
		trainees: 'trainees',
		files: 'files'
	};

	const { learningKit, tab } = $props();

	let group = $state(tab);

	function handleTabChange(e: { value: string }) {
		group = e.value;
		goto(`/learningkit/${learningKit.uuid}?tab=${e.value}`);
	}
</script>

<Tabs value={group || tabs.learningUnits} onValueChange={(e) => handleTabChange(e)}>
	{#snippet list()}
		<Tabs.Control value={tabs.learningUnits}>
			{#snippet lead()}
				<BookText size={20} />
			{/snippet}
			{$_('learningKit.learningUnits')}
		</Tabs.Control>
		<Tabs.Control value={tabs.trainees}>
			{#snippet lead()}
				<Users size={20} />
			{/snippet}
			{$_('common.trainees')}
		</Tabs.Control>
		<Tabs.Control value={tabs.files}>
			{#snippet lead()}
				<Files size={20} />
			{/snippet}
			{$_('sidebar.files')}
		</Tabs.Control>
	{/snippet}
	{#snippet content()}
		<Tabs.Panel value={tabs.learningUnits}>
			<LearningUnitsTab learningKitId={learningKit.uuid} learningUnits={learningKit.learningUnits}
			></LearningUnitsTab>
		</Tabs.Panel>
		<Tabs.Panel value={tabs.trainees}>
			<TraineesTab learningKitId={learningKit.uuid} participants={learningKit.participants}
			></TraineesTab>
		</Tabs.Panel>
		<Tabs.Panel value={tabs.files}>
			<FilesTab learningKitId={learningKit.uuid} files={learningKit.files ?? []} />
		</Tabs.Panel>
	{/snippet}
</Tabs>
