<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getAllFiles } from '$lib/api/collections/file.js';
	import PageContainer from '$lib/components/PageContainer.svelte';

	const { data } = $props();

	const filesQuery = createQuery({
		queryKey: ['files-list'],
		queryFn: () => api(fetch).req(getAllFiles, null).parse()
	});
</script>

<PageContainer title={$_('file.title')}>
	<div class="container flex space-y-4">
		<div class="container flex h-36 flex-wrap gap-2">
			{#if $filesQuery.isLoading}
				<p>Loading files...</p>
			{:else if $filesQuery.isError}
				<p>Error loading files.</p>
			{:else if $filesQuery.data?.length === 0}
				<p>No files available.</p>
			{:else}
				{#each $filesQuery.data?.map( (file) => ({ uuid: file.uuid, label: `${file.name}` }) ) ?? []}{/each}
			{/if}
		</div>
	</div>
</PageContainer>
