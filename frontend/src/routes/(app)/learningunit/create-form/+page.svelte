<script lang="ts">
	import { getToastContext } from '$lib/states/toastContext.svelte';

	const toast = getToastContext();

	let learningUnitName: string = '';

	async function handleSubmit(event: SubmitEvent) {
		event.preventDefault();
		try {
			// await createLearningUnit(learningUnitName);
		} catch (error) {
			toast.create({
				title: 'Error',
				description: 'Failed to create learning unit',
				type: 'error'
			});
		}
	}
</script>

<div class="flex h-full items-center justify-center">
	<form onsubmit={handleSubmit} class="mx-auto max-w-md rounded-lg bg-white p-6 shadow-md">
		<h2 class="mb-4 text-2xl font-bold">Create Learning Unit</h2>
		<div class="mb-4">
			<label for="learningUnitName" class="mb-1 block text-sm font-medium">Learning Unit Name</label
			>
			<input
				id="learningUnitName"
				type="text"
				bind:value={learningUnitName}
				placeholder="Enter learning unit name"
				required
				class="focus:ring-primary-500 w-full rounded border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2"
			/>
		</div>

		<div>
			<button
				type="submit"
				class="bg-primary-500 hover:bg-primary-600 focus:ring-primary-500 w-full rounded px-4 py-2 font-medium text-white focus:outline-none focus:ring-2"
			>
				Create
			</button>
		</div>
	</form>
</div>
