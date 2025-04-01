<script lang="ts">
	import Dialog from './Dialog.svelte';
	let question = '';
	let answer1 = '';
	let answer2 = '';
	let answer3 = '';
	let answer4 = '';

	const addQuestion = () => {
		// Hier fügst du die Logik ein, um die Frage und Antworten zu verarbeiten
		console.log({ question, answer1, answer2, answer3, answer4 });

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
		>Add new multiple choice block</button
	>
	<dialog bind:this={dialog} class=" preset-filled-primary-300-700 m-auto p-4">
		<div class="grid grid-cols-12 items-center justify-items-center gap-4">
			<input
				type="text"
				placeholder="Type Question"
				bind:value={question}
				class="col-span-12 mb-4 w-full border p-2"
			/>
			<input type="checkbox" id="correct" name="correct" value="correct" class="col-span-1" />
			<input
				type="text"
				placeholder="add answer 1"
				bind:value={answer1}
				class="col-span-5 border p-2"
			/>
			<input type="checkbox" id="correct" name="correct" value="correct" class="col-span-1" />
			<input
				type="text"
				placeholder="add answer 2"
				bind:value={answer2}
				class="col-span-5 border p-2"
			/>
			<input type="checkbox" id="correct" name="correct" value="correct" class="col-span-1" />
			<input
				type="text"
				placeholder="add answer 3 (optional)"
				bind:value={answer3}
				class="col-span-5 border p-2"
			/>
			<input type="checkbox" id="correct" name="correct" value="correct" class="col-span-1" />
			<input
				type="text"
				placeholder="add answer 4 (optional)"
				bind:value={answer4}
				class="col-span-5 border p-2"
			/>
			<button on:click={addQuestion} class="preset-filled-primary-50-950 mt-4 rounded p-2">
				Add multiple choice block
			</button>
		</div>
	</dialog>
</div>
