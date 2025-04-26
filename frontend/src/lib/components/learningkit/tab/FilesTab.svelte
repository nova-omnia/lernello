<script lang="ts">
	import MultiSelect from '$lib/components/MultiSelect.svelte';
	import FileUpload from '$lib/components/FileUpload.svelte';
	import { _ } from 'svelte-i18n';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getAllFiles } from '$lib/api/collections/file';
	import type { UpdateLearningKit } from '$lib/schemas/request/UpdateLearningKit';
	import { updateLearningKit } from '$lib/api/collections/learningKit';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import FileItem from '$lib/components/learningkit/displays/FileItem.svelte';

	const invalidate = useQueryInvalidation();

	interface FilesTabProps {
		learningKitId: string;
		files: {
			uuid: string;
			name: string;
			createdAt: string;
			updatedAt: string;
			size: number;
			type: string;
		}[];
	}

	const { learningKitId, files }: FilesTabProps = $props();

	const availableFilesQuery = createQuery({
		queryKey: ['files-list'],
		queryFn: () => api(fetch).req(getAllFiles, null).parse()
	});

	const updateLearningKitMutation = createMutation({
		mutationFn: ({ id, data }: { id: string; data: UpdateLearningKit }) =>
			api(fetch).req(updateLearningKit, data, id).parse(),
		onSuccess: () => {
			invalidate(['latest-learning-kits-list']);
			invalidate(['all-learning-kits-list']);
			invalidate(['learning-kit', learningKitId]);
		}
	});
</script>

<div class="flex w-full flex-col gap-4 p-4">
	<div class="flex w-full justify-between gap-4">
		<div>
			<h2 class="preset-typo-subtitle">{$_('common.files')}</h2>
			<p>{$_('learningKit.context.description')}</p>
		</div>
		<FileUpload
			onFileUploaded={(fileInfo) => {
				$updateLearningKitMutation.mutate({
					id: learningKitId,
					data: {
						files: [...files.map((file) => file.uuid), fileInfo.uuid]
					}
				});
			}}
		/>
	</div>
	<div class="flex flex-col gap-4">
		<MultiSelect
			options={$availableFilesQuery.data?.map((file) => ({
				uuid: file.uuid,
				label: `${file.name}`
			})) ?? []}
			selected={files.map((file) => ({
				//gives a warning when not using ?
				uuid: file.uuid,
				label: `${file.name}`
			}))}
			onSelect={(options) => {
				$updateLearningKitMutation.mutate({
					id: learningKitId,
					data: {
						files: options.map((options) => options.uuid)
					}
				});
			}}
		/>
		<div class="flex flex-col gap-1">
			{#each files as file (file.uuid)}
				<FileItem
					File={file}
					onRemoveFile={() => {
						const updated = files.filter((f) => f.uuid !== file.uuid).map((f) => f.uuid);

						$updateLearningKitMutation.mutate({
							id: learningKitId,
							data: {
								files: updated
							}
						});
					}}
				/>
			{/each}
		</div>
	</div>
</div>
