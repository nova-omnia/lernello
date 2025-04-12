<script lang="ts">
	import { GripVertical, Lock } from 'lucide-svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';

	let showDeleteDialog = $state(false);

	function deleteCheckpoint() {
		// if (!checkpoint) return;

		// await browserApiClient.req(removeCheckpoint, null, checkpoint.uuid);
		// await invalidate('checkpoints:list');

		// showDeleteDialog = false;
	}
</script>

<div class="flex items-center p-1">
	<GripVertical color="gray" class="h-10 w-10" />
	<div
		class="preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center rounded-lg border-[1px] p-3 text-base"
	>
		<div class="flex items-start">
			<Lock class="h-10 w-10" />
			<div class="ml-2 flex flex-col justify-center">
				<p class="text-black-700 text-xs font-bold">Checkpoint</p>
				<p class="text-xs text-gray-700">
					Trainees must complete all the above Learning Units, before gaining access to the rest.
				</p>
			</div>
		</div>

		<button
			type="button"
			class="btn preset-filled-error-500 ml-auto rounded-full p-2"
			onclick={(e) => {
				e.preventDefault();
				showDeleteDialog = true;
			}}
			>Delete
		</button>
	</div>
</div>

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
