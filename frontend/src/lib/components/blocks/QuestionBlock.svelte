<script lang="ts">
	import Dialog from '../Dialog.svelte';
	let question = '';
	let answer = '';

	const addQuestion = () => {
		// Hier fügst du die Logik ein, um die Frage und Antworten zu verarbeiten
		console.log({ question, answer });

		// Optional: Daten an dein Spring Boot Backend senden
		// fetch('/api/questions', { ... });
	};
	let dialog: Dialog;
	export const openDialog = () => {
		if (dialog) dialog.showModal();
	};
</script>

<div>
	<button class="preset-filled-primary-100-900 m-auto p-5" on:click={openDialog}
		>Add new question block</button
	>
	<dialog bind:this={dialog} class=" preset-filled-primary-500 m-auto p-4">
		<input
			type="text"
			placeholder="Type Question"
			bind:value={question}
			class="mb-4 w-full border p-2"
		/>
		<div class="grid items-center justify-items-center gap-4">
			<input type="text" placeholder="add answer" bind:value={answer} class="border p-2" />
		</div>
		<button on:click={addQuestion} class="preset-filled-primary-50-950 mt-4 rounded p-2">
			Add question block
		</button>
	</dialog>
</div>
