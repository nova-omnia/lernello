<script lang="ts">
	import { Avatar, Progress } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';
	import type { TraineeUser } from '$lib/schemas/response/TraineeUser';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { createMutation } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { resetUserPasswordAPI } from '$lib/api/collections/user';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import { toaster } from '$lib/states/toasterState.svelte';
	import { CheckCircle2 } from 'lucide-svelte';

	interface UserItemProps {
		user: TraineeUser;
		isUsersView?: boolean;
		onRemoveUser: () => void;
		showProgress?: boolean;
		progressPercentage?: number;
		isCompleted?: boolean;
		canEdit?: boolean;
	}

	const invalidate = useQueryInvalidation();
	const {
		user,
		isUsersView = false,
		onRemoveUser,
		showProgress = false,
		progressPercentage,
		isCompleted,
		canEdit = true
	}: UserItemProps = $props();

	let showDeleteDialog = $state(false);
	let showUserPasswordResetDialog = $state(false);

	const resetUserPasswordMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(resetUserPasswordAPI, null, id).parse(),
		onSuccess: () => {
			invalidate(['learning-kit']);
			toaster.create({
				description: $_('users.overview.reset.loading'),
				type: 'success'
			});
		},
		onError: () => {
			toaster.create({
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

<div
	class="card preset-filled-surface-100-900 flex w-full items-center justify-between p-4 {showProgress &&
	isCompleted
		? 'border-l-4 border-green-500'
		: 'border-surface-100-900 border-l-4'} {canEdit
		? ''
		: 'cursor-not-allowed opacity-50 select-none'}"
	title={!canEdit ? $_('users.overview.cannot_edit') : ''}
	aria-disabled={!canEdit}
>
	<div class="flex w-full max-w-sm items-center gap-4 truncate">
		<div class="relative">
			<Avatar name="{user.surname} {user.name}" classes="h-10 w-10" />
			{#if showProgress && isCompleted}
				<CheckCircle2
					class="bg-surface-100-900 absolute -top-1 -right-1 h-4 w-4 rounded-full text-green-500"
				/>
			{/if}
		</div>
		<div class="flex flex-col">
			<p class="truncate font-bold">{user.surname} {user.name}</p>
			<p class="truncate text-sm text-gray-500">{user.username}</p>
		</div>
	</div>

	{#if showProgress && progressPercentage !== undefined}
		<div class="flex items-center space-x-4">
			<div class="w-24 text-right">
				<Progress
					value={progressPercentage}
					max={100}
					height="h-2"
					meterBg={isCompleted ? 'bg-green-500' : 'bg-primary-500'}
				/>
				<span class="text-surface-500-400 text-xs">{progressPercentage}%</span>
			</div>
		</div>
	{:else}
		<div class="flex gap-2 {canEdit ? '' : 'pointer-events-none'}">
			{#if isUsersView}
				<a href={`/users/${user.uuid}/edit-form`} class="btn preset-outlined-surface-500">
					{$_('common.edit')}
				</a>
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
				{#if !isUsersView}
					{$_('common.remove')}
				{/if}
				{#if isUsersView}
					{$_('common.delete')}
				{/if}
			</button>
		</div>
	{/if}
</div>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title={isUsersView ? $_('common.user.delete') : $_('common.user.remove')}
	message={isUsersView
		? $_('dialog.delete.message', { values: { name: user.surname + ' ' + user.name } })
		: $_('dialog.remove.message', { values: { name: user.surname + ' ' + user.name } })}
	confirmText={isUsersView ? $_('common.delete') : $_('common.remove')}
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
