<script lang="ts">
	import { browserApiClient } from '$lib/api/browserApiClient';
	import { getAllTrainees } from '$lib/api/collections/user';
	import type { ParticipantUser } from '$lib/schemas/response/ParticipantUser';
	import { SquarePlus } from 'lucide-svelte';
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';
	import AddTraineeModal from './AddTraineeModal.svelte';

	interface TraineeSelectModalProps {
		isOpen: boolean;
		onSelect: (uuids: string[]) => void;
		onClose: () => void;
	}

	const { isOpen, onSelect, onClose }: TraineeSelectModalProps = $props();

	let existingTrainees = $state<ParticipantUser[]>([]);
	let selectedTrainees = $state<string[]>([]);
	let isAddTraineeModalOpen = $state<boolean>(false);

	$effect(() => {
		if (isOpen) {
			async function asyncWrapper() {
				const data = await browserApiClient.req(getAllTrainees, null);
				existingTrainees = data;
			}
			asyncWrapper();
		}
	});

	async function handleAddTrainee(username: string, name: string, surname: string) {
		console.log('Adding trainee:');
		existingTrainees = [
			...existingTrainees,
			{ uuid: crypto.randomUUID(), username, name, surname }
		];
		isAddTraineeModalOpen = false;
	}
</script>

<Modal
	open={isOpen}
	contentBase="card bg-surface-100-900 p-4 space-y-4 shadow-xl max-w-screen-sm"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<div class="bg-opacity-50 fixed inset-0 z-50 flex items-center justify-center">
			<div class="w-full max-w-3xl rounded p-6 shadow-xl">
				<h2 class="mb-4 text-lg font-bold">{$_('selectTrainees')}</h2>

				<div class="max-h-64 overflow-auto">
					<table class="table w-full">
						<thead>
							<tr>
								<th>{$_('select')}</th>
								<th>{$_('username')}</th>
								<th>{$_('name')}</th>
								<th>{$_('surname')}</th>
							</tr>
						</thead>
						<tbody>
							{#each existingTrainees as trainee (trainee.uuid)}
								<tr>
									<td>
										<input type="checkbox" bind:group={selectedTrainees} value={trainee.uuid} />
									</td>
									<td>{trainee.username}</td>
									<td>{trainee.name}</td>
									<td>{trainee.surname}</td>
								</tr>
							{/each}
						</tbody>
					</table>
				</div>

				<div class="mt-4 flex items-center justify-between">
					<button
						class="btn btn-secondary ml-0 flex items-center gap-2"
						onclick={() => {
							isAddTraineeModalOpen = true;
						}}
					>
						<SquarePlus class="size-6" />
					</button>
					<div class="flex gap-2">
						<button
							class="btn"
							onclick={() => {
								onClose();
								selectedTrainees = [];
							}}>{$_('cancel')}</button
						>
						<button
							class="btn btn-primary"
							onclick={() => {
								onSelect(selectedTrainees);
								selectedTrainees = [];
							}}>{$_('addSelected')}</button
						>
					</div>
				</div>
			</div>
		</div>
	{/snippet}
</Modal>

<AddTraineeModal
	isOpen={isAddTraineeModalOpen}
	onConfirm={handleAddTrainee}
	onCancel={() => (isAddTraineeModalOpen = false)}
/>
