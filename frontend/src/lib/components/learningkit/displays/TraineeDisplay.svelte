<script lang="ts">
	import { Avatar } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';
	import type { ParticipantUser } from '$lib/schemas/response/ParticipantUser';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';

	interface TraineeDisplayProps {
		user: ParticipantUser;
		onRemoveTrainee: () => void;
	}

	const { user, onRemoveTrainee }: TraineeDisplayProps = $props();

	let showDeleteDialog = $state(false);

	function removeTrainee() {
		showDeleteDialog = false;
	}
</script>

<div
	class="preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center rounded-lg border-[1px] p-3 text-base"
>
	<Avatar name="{user.surname} {user.name}" classes="h-11 w-11" />

	<p class="text-black-700 ml-3 text-xs font-bold">{user.surname} {user.name}</p>
	<!-- todo> -->
	<!-- <button type="button" class="btn preset-outlined-surface-500 bg-gray ml-auto rounded-full p-2"
		>{$_('common.edit')}</button
	>-->
	<button
		type="button"
		class="btn preset-filled-error-500 ml-auto rounded-full p-2"
		onclick={(e) => {
			e.preventDefault();
			showDeleteDialog = true;
		}}>{$_('common.remove')}</button
	>
</div>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title="Confirm Deletion"
	message="Are you sure you want to remove this trainee?"
	confirmText="Delete"
	danger={true}
	onConfirm={() => {
		removeTrainee();
		onRemoveTrainee();
	}}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
