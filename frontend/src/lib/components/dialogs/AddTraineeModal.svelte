<script lang="ts">
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';

	interface AddTraineeModalProps {
		isOpen: boolean;
		onConfirm: (username: string, name: string, surname: string) => void;
		onCancel: () => void;
	}

	let username = $state<string>('');
	let name = $state<string>('');
	let surname = $state<string>('');

	const { isOpen = false, onConfirm, onCancel }: AddTraineeModalProps = $props();
</script>

<Modal
	open={isOpen}
	contentBase="card bg-surface-100-900 p-4 space-y-4 shadow-xl max-w-screen-sm"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<div class="space-y-4">
			<h2 class="text-lg font-bold">Add Trainee</h2>
			<div class="space-y-2">
				<label class="block">
					<span class="text-sm font-medium">Username</span>
					<input
						type="text"
						bind:value={username}
						class="input input-bordered w-full"
						placeholder="Enter username"
					/>
				</label>
				<label class="block">
					<span class="text-sm font-medium">Name</span>
					<input
						type="text"
						bind:value={name}
						class="input input-bordered w-full"
						placeholder="Enter name"
					/>
				</label>
				<label class="block">
					<span class="text-sm font-medium">Surname</span>
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
					}}>Cancel</button
				>
				<button
					class="btn btn-primary"
					onclick={() => {
						onConfirm(username, name, surname);
						username = '';
						name = '';
						surname = '';
					}}>Submit</button
				>
			</div>
		</div>
	{/snippet}
</Modal>
