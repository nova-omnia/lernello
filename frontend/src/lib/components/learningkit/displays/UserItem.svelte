<script lang="ts">
	import { Avatar } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';
	import type { ParticipantUser } from '$lib/schemas/response/ParticipantUser';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { goto } from '$app/navigation';
	import { createMutation } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { resetUserPasswordAPI } from '$lib/api/collections/user';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import { toaster } from '$lib/states/toasterState.svelte';

	interface UserItemProps {
		user: ParticipantUser;
		usersView?: boolean;
		onRemoveUser: () => void;
		onResetUser?: () => void;
	}

	const invalidate = useQueryInvalidation();
	const { user, usersView = true, onRemoveUser }: UserItemProps = $props();

	let showDeleteDialog = $state(false);
	let showUserPasswordResetDialog = $state(false);

	const resetUserPasswordMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(resetUserPasswordAPI, null, id).parse(),
		onMutate: () => {
			toaster.create({
				title: $_('common.loading'),
				description: $_('files.upload.uploading.description'),
				type: 'info'
			});
		},
		onSuccess: () => {
			invalidate(['learning-kit']);
			toaster.create({
				title: $_('common.loading'),
				description: $_('users.overview.reset.loading'),
				type: 'success'
			});
		},
		onError: () => {
			toaster.create({
				title: $_('common.error.title'),
				description: $_('users.overview.reset.error'),
				type: 'error'
			});
		}
	});

	function removeUser() {
		onRemoveUser();
		showDeleteDialog = false;
	}

	function resetUserPassword() {
		showUserPasswordResetDialog = false;
		$resetUserPasswordMutation.mutate(user.uuid);
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
		{#if usersView}
			<button
				type="button"
				class="btn preset-outlined-surface-500"
				onclick={() => goto(`/users/${user.uuid}/edit-form`)}
			>
				{$_('common.edit')}
			</button>
			<button
				type="button"
				class="btn preset-outlined-error-500 text-error-500"
				onclick={(e) => {
					e.preventDefault();
					showUserPasswordResetDialog = true;
				}}
			>
				{$_('common.reset.password')}
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
			{#if !usersView}
				{$_('common.remove')}
			{/if}
			{#if usersView}
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

<ConfirmDialog
	isOpen={showUserPasswordResetDialog}
	title={$_('common.user.reset')}
	message={$_('dialog.reset.message', { values: { name: user.surname + ' ' + user.name } })}
	confirmText={$_('dialog.reset.confirm')}
	cancelText={$_('common.cancel')}
	danger={true}
	onConfirm={resetUserPassword}
	onCancel={() => {
		showUserPasswordResetDialog = false;
	}}
/>
