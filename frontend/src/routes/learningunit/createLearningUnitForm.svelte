<script lang="ts">
	let learningUnitName: string = '';
	let error: string = '';

	interface LearningUnit {
		uuid: string;
	}

	async function handleSubmit(event: Event): Promise<void> {
		event.preventDefault();
		error = '';

		if (!learningUnitName.trim()) {
			error = 'Learning Unit name is required.';
			return;
		}

		try {
			const response = await fetch('/api/learning-units', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ name: learningUnitName.trim() })
			});
			if (!response.ok) {
				throw new Error('Failed to create');
			}
			const createdUnit: LearningUnit = await response.json();
			window.location.href = `/edit/${createdUnit.uuid}`;
		} catch (e: unknown) {
			if (e instanceof Error) {
				error = e.message;
			} else {
				error = 'An unknown error occurred';
			}
		}
	}
</script>

<div class="flex h-full items-center justify-center">
	<form
		on:submit|preventDefault={handleSubmit}
		class="mx-auto max-w-md rounded-lg bg-white p-6 shadow-md"
	>
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
				class="focus:ring-primary-500 w-full rounded border border-gray-300 px-3 py-2 focus:ring-2 focus:outline-none"
			/>
		</div>
		{#if error}
			<div class="mb-4">
				<p class="text-red-500">{error}</p>
			</div>
		{/if}
		<div>
			<button
				type="submit"
				class="bg-primary-500 hover:bg-primary-600 focus:ring-primary-500 w-full rounded px-4 py-2 font-medium text-white focus:ring-2 focus:outline-none"
			>
				Create
			</button>
		</div>
	</form>
</div>
