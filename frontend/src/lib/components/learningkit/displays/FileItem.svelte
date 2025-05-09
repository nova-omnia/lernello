<script lang="ts">
	import { File as FileIcon, X as XIcon } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
	import type { FileRes } from '$lib/schemas/response/FileRes';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { BASE_URL } from '$lib/api/apiClient';

	interface FileItemProps {
		file: FileRes;
		isFilesView?: boolean;
		onRemoveFile: () => void;
		isForModal?: boolean;
	}

	const { file, isFilesView = false, onRemoveFile, isForModal = false }: FileItemProps = $props();

	let showDeleteDialog = $state(false);

	function removeFile() {
		onRemoveFile();
		showDeleteDialog = false;
	}

	function getStaticFileUrl(fileId: string) {
		return BASE_URL + '/files/' + fileId;
	}
</script>

<div class="card preset-filled-surface-100-900 flex w-full items-center justify-between p-4">
	<div class="flex w-full max-w-sm items-center gap-4 truncate">
		<FileIcon class="h-10 w-10" />
		<p class="text-black-700 ml-3 text-xs font-bold">{file.name}</p>
	</div>

	<div>
		{#if !isForModal}
			<a class="btn preset-outlined-surface-500" href={getStaticFileUrl(file.uuid)} target="_blank">
				{$_('common.open')}
			</a>
			<button
				type="button"
				class="btn preset-filled-error-500"
				onclick={(e) => {
					e.preventDefault();
					showDeleteDialog = true;
				}}
			>
				{#if isFilesView}
					{$_('common.delete')}
				{:else}
					{$_('common.remove')}
				{/if}
			</button>
		{:else}
			<button
				type="button"
				class="btn-icon"
				onclick={(e) => {
					e.preventDefault();
					onRemoveFile();
				}}
			>
				<XIcon class="h-5 w-5" />
			</button>
		{/if}
	</div>
</div>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title={isFilesView ? $_('files.delete.title') : $_('files.remove.title')}
	message={isFilesView
		? $_('files.delete.message', { values: { name: file.name } })
		: $_('files.remove.message', { values: { name: file.name } })}
	confirmText={isFilesView ? $_('common.delete') : $_('common.remove')}
	cancelText={$_('common.cancel')}
	danger={true}
	onConfirm={removeFile}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
