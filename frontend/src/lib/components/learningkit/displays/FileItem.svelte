<script lang="ts">
	import { File as FileIcon } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
	import type { FileRes } from '$lib/schemas/response/FileRes';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { BASE_URL } from '$lib/api/apiClient';

	interface FileItemProps {
		File: FileRes;
		onRemoveFile: () => void;
	}

	const { File, onRemoveFile }: FileItemProps = $props();

	let showDeleteDialog = $state(false);

	function removeFile() {
		onRemoveFile();
		showDeleteDialog = false;
	}

	const fileId = File.uuid;
	function getStaticFileUrl(fileId: string) {
		return BASE_URL + '/files/' + fileId;
	}
</script>

<div class="card preset-filled-surface-100-900 flex w-full items-center justify-between p-4">
	<div class="flex w-full max-w-sm items-center gap-4 truncate">
		<FileIcon class="h-10 w-10" />
		<p class="text-black-700 ml-3 text-xs font-bold">{File.name}</p>
	</div>

	<div>
		<a href={getStaticFileUrl(File.uuid)} class="btn preset-tonal-surface">
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
			{$_('common.remove')}
		</button>
	</div>
</div>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title={$_('blocks.delete_title')}
	message={$_('blocks.delete_message')}
	confirmText={$_('common.delete')}
	cancelText={$_('common.cancel')}
	danger={true}
	onConfirm={removeFile}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
