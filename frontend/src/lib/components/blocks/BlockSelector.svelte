<script lang="ts">
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
</script>

<div class="preset-filled-surface-50-950 space-y-4 p-4">
	<h2 class="h2">Actions</h2>
	<button
		type="button"
		class="btn preset-filled-primary-500 block w-full"
		onclick={() => {
			queueBlockAction({
				type: 'ADD_BLOCK',
				data: {
					name: 'Theory Block',
					type: 'theory'
				}
			});
		}}
	>
		Theory Block
	</button>
	<button
		type="button"
		class="btn preset-filled-primary-500 block w-full"
		onclick={() => {
			queueBlockAction({
				type: 'ADD_BLOCK',
				data: {
					name: 'Multiple Choice Quiz',
					type: 'quiz'
				}
			});
		}}
	>
		Multiple Choice Quiz
	</button>
	<button
		type="button"
		class="btn preset-filled-primary-500 block w-full"
		onclick={() => {
			// queueLearningUnitAction({
			// 	type: 'ADD_BLOCK',
			// 	data: {
			// 		name: 'Text Answer Quiz',
			// 		type: 'textAnswer'
			// 	}
			// });
			alert(
				'Text Answer Quiz is not implemented yet. Please use the Multiple Choice Quiz for now.'
			);
		}}
	>
		Text Answer Quiz
	</button>
</div>
