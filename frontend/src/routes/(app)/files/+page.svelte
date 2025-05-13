<script lang="ts">
	import FileItem from '$lib/components/learningkit/displays/FileItem.svelte';
	import { _ } from 'svelte-i18n';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getAllFiles } from '$lib/api/collections/file';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import { deleteFile } from '$lib/api/collections/file.js';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';
	import { error } from '@sveltejs/kit';
	import FileUpload from '$lib/components/FileUpload.svelte';
	import { toaster } from '$lib/states/toasterState.svelte';

	const { data } = $props();
	const invalidate = useQueryInvalidation();

	const filesQuery = createQuery({
		queryKey: ['files-overview'],
		queryFn: () => api(fetch).req(getAllFiles, null).parse()
	});

	const deleteFileMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(deleteFile, null, id).parse(),
		onSuccess: () => {
			invalidate(['files-overview']);
			invalidate(['learning-kit']);
			toaster.create({
				title: $_('common.success.title'),
				description: $_('files.delete.success'),
				type: 'success'
			});
		},
		onError: () => {
			toaster.create({
				title: $_('common.error'),
				description: $_('files.delete.error'),
				type: 'error'
			});
		}
	});
</script>

{#if data.userInfo.role === INSTRUCTOR_ROLE}
	<div class="flex w-full flex-col gap-8 p-4">
		<div class="flex w-full justify-between gap-4">
			<div>
				<h1 class="preset-typo-headline mb-4">{$_('common.files')}</h1>
				<p>{$_('learningKit.context.description')}</p>
			</div>
			<FileUpload onFileUploaded={() => invalidate(['learning-kit'])} />
		</div>
		<div class="flex flex-col gap-1">
			{#if $filesQuery.status === 'pending'}
				{#each Array(3)}
					<div class="card preset-filled-surface-100-900 h-20 w-full gap-2 p-5">
						<div class="placeholder h-full animate-pulse"></div>
					</div>
				{/each}
			{:else if $filesQuery.status === 'error'}
				<ErrorIllustration>{$_('trainees.error.loadList')}</ErrorIllustration>
			{:else}
				{#each $filesQuery.data as file (file.uuid)}
					<FileItem
						{file}
						isFilesView={true}
						onRemoveFile={() => $deleteFileMutation.mutate(file.uuid)}
					/>
				{/each}
			{/if}
		</div>
	</div>
{:else}
	{error(403, 'You are not allowed to view this page')}
{/if}
