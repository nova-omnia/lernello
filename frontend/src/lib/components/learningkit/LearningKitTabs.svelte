<script lang="ts">
	import { Tabs } from '@skeletonlabs/skeleton-svelte';
	import { BookText, Files, Users } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';

	let group = $state($_('learningKit.learningUnits'));
</script>

<Tabs value={group} onValueChange={(e) => (group = e.value)}>
	{#snippet list()}
		<Tabs.Control value={$_('learningKit.learningUnits')}>
			{#snippet lead()}
				<BookText size={20} />
			{/snippet}
			{$_('learningKit.learningUnits')}
		</Tabs.Control>
		<Tabs.Control value={$_('common.trainees')}>
			{#snippet lead()}
				<Users size={20} />
			{/snippet}
			{$_('common.trainees')}
		</Tabs.Control>
		<Tabs.Control value={$_('sidebar.files')}>
			{#snippet lead()}
				<Files size={20} />
			{/snippet}
			{$_('sidebar.files')}
		</Tabs.Control>
	{/snippet}
	{#snippet content()}
		<Tabs.Panel value={$_('learningKit.learningUnits')}></Tabs.Panel>
		<Tabs.Panel value={$_('common.trainees')}>Boat Panel - {lorem}</Tabs.Panel>
		<Tabs.Panel value={$_('sidebar.files')}>Car Panel - {lorem}</Tabs.Panel>
	{/snippet}
</Tabs>
