<script lang="ts">
	import { queueLearningUnitAction } from '$lib/states/blockActionState';
</script>

<div class="preset-filled-surface-50-950 space-y-4 border-r border-gray-300 p-4">
	<h2 class="mb-4 text-lg font-bold">Actions</h2>
	<button
		type="button"
		class="bg-primary-500 hover:bg-primary-600 w-full rounded-xl px-4 py-2 text-white"
		onclick={() => {
			queueLearningUnitAction({
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
		class="bg-primary-500 hover:bg-primary-600 w-full rounded-xl px-4 py-2 text-white"
		onclick={() => {
			queueLearningUnitAction({
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
		class="bg-primary-500 hover:bg-primary-600 w-full rounded-xl px-4 py-2 text-white"
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
