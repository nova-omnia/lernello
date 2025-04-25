<!--AddTraineeModal.svelte-->
<script lang="ts">
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import { addTrainee } from '$lib/api/collections/user';
	import { api } from '$lib/api/apiClient';
	import { _ } from 'svelte-i18n';

	interface AddTraineeModalProps {
		isOpen: boolean;
		onConfirm: () => void;
		onCancel: () => void;
	}

	let username = $state<string>('');
	let name = $state<string>('');
	let surname = $state<string>('');

	const { isOpen = false, onConfirm, onCancel }: AddTraineeModalProps = $props();

	async function handleAddTrainee() {
		const createdTrainee = { username, name, surname };
		await api(fetch).req(addTrainee, createdTrainee).parse();
	}
</script>

<Modal
	open={isOpen}
	contentBase="card bg-surface-100-900 p-4 space-y-4 shadow-xl max-w-3xl w-full"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<h2 class="text-lg font-bold">{$_('addTrainee')}</h2>
		<div class="space-y-2">
			<label class="block">
				<span class="text-sm font-medium">{$_('common.username')}</span>
				<input
					type="text"
					bind:value={username}
					class="input input-bordered w-full"
					placeholder="Enter username"
				/>
			</label>
			<label class="block">
				<span class="text-sm font-medium">{$_('common.name')}</span>
				<input
					type="text"
					bind:value={name}
					class="input input-bordered w-full"
					placeholder="Enter name"
				/>
			</label>
			<label class="block">
				<span class="text-sm font-medium">{$_('common.surname')}</span>
				<input
					type="text"
					bind:value={surname}
					class="input input-bordered w-full"
					placeholder="Enter surname"
				/>
			</label>
		</div>
		<div class="flex justify-end space-x-2">
			<button
				class="btn btn-secondary"
				onclick={() => {
					onCancel();
					username = '';
					name = '';
					surname = '';
				}}
				>Cancel
			</button>
			<button
				class="btn btn-primary"
				onclick={async () => {
					await handleAddTrainee();
					onConfirm();
				}}
				>Submit
			</button>
		</div>
	{/snippet}
</Modal>
