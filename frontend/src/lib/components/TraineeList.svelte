<script lang="ts">
	import { writable } from 'svelte/store';
	import { UserRoundPlus } from 'lucide-svelte';

	let trainees = $props()

	// dummy data for trainees
	trainees = writable([
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

<div class="blox p-4 bg-s">
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
</div>
