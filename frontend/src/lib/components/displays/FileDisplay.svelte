<script lang="ts">
	import { File as FileIcon } from 'lucide-svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { browserApiClient } from '$lib/api/browserApiClient';
	import { invalidate } from '$app/navigation';
	import { deleteFile } from '$lib/api/collections/file';
	import { _ } from 'svelte-i18n';
	const { File } = $props();

	let showDeleteDialog = $state(false);

	async function removeFile() {
		if (!File) return;

		await browserApiClient.req(deleteFile, null, File.uuid);
		await invalidate('files:list');

		showDeleteDialog = false;
	}
</script>

<div
	class="File-Display preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center rounded-lg border-[1px] p-3 text-base"
>
	<FileIcon class="h-10 w-10" />
	<p class="text-black-700 ml-3 text-xs font-bold">{File.name}</p>
	<button
		type="button"
		onclick={(e) => {
			e.preventDefault();
			showDeleteDialog = true;
		}}
		class="btn preset-filled-error-500 ml-auto rounded-full p-2">{$_('button.remove')}</button
	>
</div>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title="Confirm Deletion"
	message={`Are you sure you want to remove "${File?.name}"?`}
	confirmText="Delete"
	danger={true}
	onConfirm={removeFile}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
