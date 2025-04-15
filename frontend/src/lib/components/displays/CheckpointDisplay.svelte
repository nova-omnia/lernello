<script lang="ts">
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _ } from 'svelte-i18n';
	import KitContentItem from './KitContentItem.svelte';

	let showDeleteDialog = $state(false);

	function deleteCheckpoint() {
		// if (!checkpoint) return;
		// await browserApiClient.req(removeCheckpoint, null, checkpoint.uuid);
		// await invalidate('checkpoints:list');
		// showDeleteDialog = false;
	}
</script>

<KitContentItem
	name={$_('learningKit.checkpoint')}
	description={$_('learningKit.checkpoint.description')}
>
	{#snippet actions()}
		<button
			type="button"
			class="btn preset-filled-error-500 rounded-full"
			onclick={(e) => {
				e.preventDefault();
				showDeleteDialog = true;
			}}
		>
			{$_('button.delete')}
		</button>
	{/snippet}
</KitContentItem>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title="Confirm Deletion"
	message="Are you sure you want to delete this checkpoint?"
	confirmText="Delete"
	danger={true}
	onConfirm={deleteCheckpoint}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
