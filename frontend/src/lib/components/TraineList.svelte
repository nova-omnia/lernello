<script lang="ts">
	import { writable } from 'svelte/store';
	import { UserRoundPlus } from 'lucide-svelte';

	// dummy data for trainees
	let trainees = writable([
		{ uuid: '1', username: 'john.doe@example.com', name: 'Doedor', surname: 'John' },
		{ uuid: '2', username: 'jane.doe@example.com', name: 'Doedor', surname: 'Jane' }
	]);

	let selectedTrainees = writable([]);

	// Function to handle adding trainees
	function addTrainees() {
		selectedTrainees.update((selected) => {
			console.log('Selected trainees:', selected);
			return [];
		});
	}
</script>

<div class="blox p-4 shadow-md">
	<h3 class="mb-4 text-lg font-bold">Trainees</h3>
	<p class="mb-4 text-sm text-gray-500">
		Select the trainees you want to give access to the course
	</p>
	<div class="table-wrap">
		<table class="table caption-bottom">
			<thead>
				<tr>
					<th>Select</th>
					<th>Username</th>
					<th>Name</th>
					<th>Surname</th>
				</tr>
			</thead>
			<tbody>
				{#each $trainees as trainee (trainee.uuid)}
					<tr>
						<td>
							<input
								class="checkbox"
								type="checkbox"
								bind:group={$selectedTrainees}
								value={trainee.uuid}
							/>
						</td>
						<td>{trainee.username}</td>
						<td>{trainee.name}</td>
						<td>{trainee.surname}</td>
					</tr>
				{/each}
			</tbody>
		</table>
	</div>
	<div class="mt-4 flex justify-center">
		<button
			type="button"
			class="btn btn-primary flex w-full max-w-md items-center justify-center gap-2"
			on:click={addTrainees}
		>
			<UserRoundPlus class="h-5 w-5" />
			<span>Add Trainees</span>
		</button>
	</div>
</div>
