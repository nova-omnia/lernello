<script lang="ts">
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	interface TraineeSelectModalProps {
		isOpen: boolean;
		onSelect: (uuids: string[]) => void;
		onClose: () => void;
	}

	const { isOpen, onSelect, onClose }: TraineeSelectModalProps = $props();

	let existingTrainees = $state<
		{ uuid: string; username: string; name: string; surname: string }[]
	>([]);
	let selectedTrainees = $state<string[]>([]);

	// browserApiClient.on('traineeSelected', (uuids: string[]) => {
	// 	selectedTrainees = uuids;
	// });

	$effect(() => {
		if (isOpen) {
			async function asyncWrapper() {
				// const data = await browserApiClient.req(getAllUsers);
				existingTrainees = [
					{ uuid: '1', username: 'john.doe@example.com', name: 'Garbor', surname: 'John' },
					{ uuid: '2', username: 'jane.doe@example.com', name: 'Doedor', surname: 'Jane' },
					{ uuid: '3', username: 'jane.doe@example.com', name: 'Max', surname: 'Sander' },
					{ uuid: '4', username: 'jane.doe@example.com', name: 'Bea', surname: 'Tilder' },
					{ uuid: '5', username: 'jane.doe@example.com', name: 'Tony', surname: 'Blair' },
					{ uuid: '6', username: 'jane.doe@example.com', name: 'John', surname: 'Clark' },
					{ uuid: '7', username: 'jane.doe@example.com', name: 'Adam', surname: 'Smith' },
					{ uuid: '8', username: 'jane.doe@example.com', name: 'Friz', surname: 'Taylor' },
					{ uuid: '9', username: 'jane.doe@example.com', name: 'Sasha', surname: 'Leon' },
					{ uuid: '10', username: 'jane.doe@example.com', name: 'Selina', surname: 'Brown' },
					{ uuid: '11', username: 'jane.doe@example.com', name: 'Carola', surname: 'Brown' },
					{ uuid: '12', username: 'jane.doe@example.com', name: 'Tom', surname: 'Frisch' }
				];
			}
			asyncWrapper();
		}
	});
</script>

<Modal
	open={isOpen}
	contentBase="card bg-surface-100-900 p-4 space-y-4 shadow-xl max-w-screen-sm"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<div class="bg-opacity-50 fixed inset-0 z-50 flex items-center justify-center bg-black">
			<div class="w-full max-w-3xl rounded bg-white p-6 shadow-xl">
				<h2 class="mb-4 text-lg font-bold">Select Trainees</h2>

				<div class="max-h-64 overflow-auto">
					<table class="table w-full">
						<thead>
							<tr>
								<th>Select</th>
								<th>Username</th>
								<th>Name</th>
								<th>Surname</th>
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

				<div class="mt-4 flex justify-end gap-2">
					<button
						class="btn"
						onclick={() => {
							onClose();
							selectedTrainees = [];
						}}>Cancel</button
					>
					<button
						class="btn btn-primary"
						onclick={() => {
							onSelect(selectedTrainees);
							selectedTrainees = [];
						}}>Add Selected</button
					>
				</div>
			</div>
		</div>
	{/snippet}
</Modal>
