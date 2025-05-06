<script lang="ts">
	import { Avatar } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';
	import type { ParticipantUser } from '$lib/schemas/response/ParticipantUser';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { goto } from '$app/navigation';

	interface UserItemProps {
		user: ParticipantUser;
		isRemove?: boolean;
		onRemoveUser: () => void;
	}

	const { user, isRemove = true, onRemoveUser }: UserItemProps = $props();

	let showDeleteDialog = $state(false);

	function removeUser() {
		onRemoveUser();
		showDeleteDialog = false;
	}
</script>

<div class="card preset-filled-surface-100-900 flex w-full items-center justify-between p-4">
	<div class="flex w-full max-w-sm items-center gap-4 truncate">
		<Avatar name="{user.surname} {user.name}" classes="h-10 w-10" />
		<div class="flex flex-col">
			<p class="truncate font-bold">{user.surname} {user.name}</p>
			<p class="truncate text-sm text-gray-500">{user.username}</p>
		</div>
	</div>
	<div class="flex gap-2">
		{#if !isRemove}
			<button
				type="button"
				class="btn preset-outlined-surface-500"
				onclick={() => goto(`/users/${user.uuid}/edit-form`)}
			>
				{$_('common.edit')}
			</button>
		{/if}
		<button
			type="button"
			class="btn preset-filled-error-500"
			onclick={(e) => {
				e.preventDefault();
				showDeleteDialog = true;
			}}
		>
			{#if isRemove}
				{$_('common.remove')}
			{/if}
			{#if !isRemove}
				{$_('common.delete')}
			{/if}
		</button>
	</div>
</div>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title={$_('common.user.delete')}
	message={$_('dialog.delete.message', { values: { name: user.surname + ' ' + user.name } })}
	confirmText={$_('common.delete')}
	cancelText={$_('common.cancel')}
	danger={true}
	onConfirm={removeUser}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
