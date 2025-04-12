<script lang="ts">
	import { Avatar } from '@skeletonlabs/skeleton-svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	const { User } = $props();

	let showDeleteDialog = $state(false);

	function removeTrainee() {
		//todo
		//if (!trainee) return;

		//await browserApiClient.req(removeTrainee, null, trainee.uuid);
		//await invalidate('trainees:list');

		showDeleteDialog = false;
	}
</script>

<div
	class="Trainee-Display preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center rounded-lg border-[1px] p-3 text-base"
>
	<Avatar name="{User.surname} {User.name}" src={User.avatar} classes="h-11 w-11" />

	<p class="text-black-700 ml-3 text-xs font-bold">{User.surname} {User.name}</p>

	<button type="button" class="btn preset-outlined-surface-500 bg-gray ml-auto rounded-full p-2"
		>Invite</button
	>
	<button
		type="button"
		class="btn preset-filled-error-500 ml-1 rounded-full p-2"
		onclick={(e) => {
			e.preventDefault();
			showDeleteDialog = true;
		}}>Remove</button
	>
</div>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title="Confirm Deletion"
	message="Are you sure you want to remove this trainee?"
	confirmText="Delete"
	danger={true}
	onConfirm={removeTrainee}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
