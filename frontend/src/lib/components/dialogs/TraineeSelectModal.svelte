<script lang="ts">
	const {
		trainees = [],
		open = false,
		onSelect,
		onClose
	} = $props<{
		trainees: { uuid: string; username: string; name: string; surname: string }[];
		open: boolean;
		onSelect: (uuids: string[]) => void;
		onClose: () => void;
	}>();

	let selectedTrainees = $state<string[]>([]);

	function addTrainees() {
		onSelect?.(selectedTrainees);
		selectedTrainees = [];
	}

	function closeModal() {
		onClose?.();
		selectedTrainees = [];
	}
</script>

{#if open}
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
						{#each trainees as trainee (trainee.uuid)}
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
				<button class="btn" onclick={closeModal}>Cancel</button>
				<button class="btn btn-primary" onclick={addTrainees}>Add Selected</button>
			</div>
		</div>
	</div>
{/if}
