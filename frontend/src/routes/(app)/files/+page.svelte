<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getAllFiles } from '$lib/api/collections/file.js';
	import PageContainer from '$lib/components/PageContainer.svelte';
	import FileItem from '$lib/components/learningkit/displays/FileItem.svelte';
	import type { FileRes } from '$lib/schemas/response/FileRes';
	import PlaceholderLearningKit from '$lib/components/learningkit/PlaceholderLearningKit.svelte';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';

	const { data } = $props();
	interface FilesListProps {
		files: FileRes[];
	}
	const { files }: FilesListProps = data;

	const filesQuery = createQuery({
		queryKey: ['files-list'],
		queryFn: () => api(fetch).req(getAllFiles, null).parse()
	});
</script>

<PageContainer title={$_('file.title')}>
	<div class="container flex space-y-4">
		<div class="container flex h-36 flex-wrap gap-2">
			{#if $filesQuery.status === 'pending'}
				{#each Array(3)}
					<PlaceholderLearningKit />
				{/each}
			{:else if $filesQuery.status === 'error'}
				<ErrorIllustration>{$_('file.error.loadList')}</ErrorIllustration>
			{:else if $filesQuery.data?.length === 0}
				<p>No files available.</p>
			{:else}
				{#each files as file (file.uuid)}
					<FileItem
						File={file}
						onRemoveFile={() => {
							const updated = files.filter((f) => f.uuid !== file.uuid).map((f) => f.uuid);
						}}
					/>
				{/each}
			{/if}
		</div>
	</div>
</PageContainer>
