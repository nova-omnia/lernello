<script lang="ts">
	import SuperDebug, { superForm } from 'sveltekit-superforms';
	import { toaster } from '$lib/states/toasterState.svelte.js';

	let { data } = $props();
	const { form, errors, constraints, enhance } = superForm(data.form, {
		onError: (error) => {
			console.error('Error:', error.result.error);
			toaster.create({
				title: 'Error',
				description: `Uh oh, something went wrong. (${error.result.status})`,
				type: 'error'
			});
		}
	});
</script>

<form method="POST" use:enhance action="?/create" class="mx-auto max-w-lg space-y-4">
	<p class="preset-typo-subtitle">Dashboard</p>
	<h1 class="h1">Create a new Learning Kit</h1>

	<label class="label">
		<span class="label-text">Name</span>
		<input
			id="name"
			type="text"
			class="input"
			name="name"
			placeholder="Learning Kit Name"
			aria-invalid={$errors.name ? 'true' : undefined}
			bind:value={$form.name}
			{...$constraints.name}
		/>
		{#if $errors.name}
			<p class="text-sm text-red-500">{$errors.name}</p>
		{/if}
	</label>

	<label class="label">
		<span class="label-text">Description <i>– Optional</i></span>
		<textarea
			id="description"
			name="description"
			bind:value={$form.description}
			class="textarea"
			placeholder="Provide a description..."
			aria-invalid={$errors.description ? 'true' : undefined}
			{...$constraints.description}
		></textarea>
		{#if $errors.description}
			<p class="text-sm text-red-500">{$errors.description}</p>
		{/if}
	</label>

	<label class="label">
		<span class="label-text">Context <i>– Optional</i></span>
		<textarea
			id="context"
			name="context"
			bind:value={$form.context}
			class="textarea"
			placeholder="Provide additional context..."
			aria-invalid={$errors.context ? 'true' : undefined}
			{...$constraints.context}
		></textarea>
		{#if $errors.context}
			<p class="text-sm text-red-500">{$errors.context}</p>
		{/if}
	</label>

	<label class="label">
		<span class="label-text">Deadline <i>– Optional</i></span>
		<input
			id="deadlineDate"
			type="datetime-local"
			name="deadlineDate"
			bind:value={$form.deadlineDate}
			class="input"
			placeholder="YYYY-MM-DD"
			aria-invalid={$errors.deadlineDate ? 'true' : undefined}
			{...$constraints.deadlineDate}
		/>
		{#if $errors.deadlineDate}
			<p class="text-sm text-red-500">{$errors.deadlineDate}</p>
		{/if}
	</label>

	<button class="btn preset-filled-primary-400-600 w-full">Create Learning Kit </button>
	<SuperDebug data={$form} />
</form>
