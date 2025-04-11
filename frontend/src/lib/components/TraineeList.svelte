<script lang="ts">
	import { writable } from 'svelte/store';
	import { UserRoundPlus } from 'lucide-svelte';

	let trainees = $props()

	// dummy data for trainees
	trainees = writable([
		{ uuid: '1', username: 'john.doe@example.com', name: 'Garbor', surname: 'John' },
		{ 
			
			uuid: '2', username: 'jane.doe@example.com', name: 'Doedor', surname: 'Jane' },
		{uuid: '3', username: 'jane.doe@example.com', name: 'Max', surname: 'Sander'},
		{uuid: '4', username: 'jane.doe@example.com', name: 'Bea', surname: 'Tilder'},
		{uuid: '5', username: 'jane.doe@example.com', name: 'Tony', surname: 'Blair'},
		{uuid: '6', username: 'jane.doe@example.com', name: 'John', surname: 'Clark'},
		{uuid: '7', username: 'jane.doe@example.com', name: 'Adam', surname: 'Smith'},
		{uuid: '8', username: 'jane.doe@example.com', name: 'Friz', surname: 'Taylor'},
		{uuid: '9', username: 'jane.doe@example.com', name: 'Sasha', surname: 'Leon'},
		{uuid: '10', username: 'jane.doe@example.com', name: 'Selina', surname: 'Brown'},
		{uuid: '11', username: 'jane.doe@example.com', name: 'Carola', surname: 'Brown'},
		{uuid: '12', username: 'jane.doe@example.com', name: 'Tom', surname: 'Frisch'}

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
