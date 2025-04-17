<script lang="ts">
	import { getAllTrainees } from '$lib/api/collections/user';
	import type { ParticipantUser } from '$lib/schemas/response/ParticipantUser';
	import { SquarePlus } from 'lucide-svelte';
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';
	import AddTraineeModal from './AddTraineeModal.svelte';
	import { api } from '$lib/api/apiClient';

	interface TraineeSelectModalProps {
		isOpen: boolean;
		onSelect: (uuids: string[]) => void;
		onClose: () => void;
		allTrainees: ParticipantUser[];
		selectedParticipants: ParticipantUser[];
	}

	let { isOpen, onSelect, onClose, allTrainees, selectedParticipants }: TraineeSelectModalProps =
		$props();

	let selectedTrainees = $state<string[]>(
		selectedParticipants.map((participant) => participant.uuid) ?? []
	);

	let isAddTraineeModalOpen = $state(false);
	let searchValue = $state('');

	const filteredTrainees = $derived(() => {
		if (!searchValue) return allTrainees;
		const lower = searchValue.toLowerCase();
		return allTrainees.filter((trainee) =>
			`${trainee.name} ${trainee.surname} ${trainee.username}`.toLowerCase().includes(lower)
		);
	});

	$effect(() => {
		if (selectedParticipants) {
			selectedTrainees = selectedParticipants.map((participant) => participant.uuid) ?? [];
		}
	});

	async function handleAddTrainee() {
		allTrainees = await api(fetch).req(getAllTrainees, null).parse();
		isAddTraineeModalOpen = false;
	}
</script>

<Modal
	open={isOpen}
	contentBase="card bg-surface-100-900 p-4 space-y-4 shadow-xl max-w-3xl w-full"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<h2 class="mb-4 text-lg font-bold">{$_('selectTrainees')}</h2>

		<input
			type="text"
			placeholder={$_('multiSelect.searchPlaceholder')}
			bind:value={searchValue}
			class="bg-surface-200-800 text-surface-800-200 w-full px-3 py-2"
		/>

		<div class="max-h-64 min-h-70 overflow-auto">
			<table class="table w-full">
				<thead>
					<tr>
						<th>{$_('multiSelect.select')}</th>
						<th>{$_('multiSelect.username')}</th>
						<th>{$_('multiSelect.name')}</th>
						<th>{$_('multiSelect.surname')}</th>
					</tr>
				</thead>
				<tbody>
					{#each filteredTrainees() as trainee (trainee.uuid)}
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
					}}>{$_('button.cancel')}</button
				>
				<button
					class="btn btn-primary"
					onclick={() => {
						onSelect(selectedTrainees);
						selectedTrainees = [];
					}}>{$_('button.addSelected')}</button
				>
			</div>
		</div>
	{/snippet}
</Modal>

<AddTraineeModal
	isOpen={isAddTraineeModalOpen}
	onConfirm={handleAddTrainee}
	onCancel={() => (isAddTraineeModalOpen = false)}
/>
