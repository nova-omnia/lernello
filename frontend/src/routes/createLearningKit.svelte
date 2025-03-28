<script lang="ts">
	import SuperDebug, { superForm } from 'sveltekit-superforms';
	import { FileUpload } from '@skeletonlabs/skeleton-svelte';
	import IconUpload from '@lucide/svelte/icons/upload';

	let { data } = $props();
	const { form, errors, enhance } = superForm(data.form);

	function removeFile() {
		//TODO implement remove file
	}
</script>

<form
	method="POST"
	use:enhance
	action="?/CreateLearningKit"
	class="mx-auto w-full max-w-lg space-y-4 p-4"
>
	<h1 class="text-2xl font-bold">Create a new Learning Kit</h1>

	<div>
		<label for="name" class="block">Name *</label>
		<input
			type="text"
			name="name"
			bind:value={$form.name}
			aria-invalid={$errors.name ? 'true' : 'false'}
			required
			class="rounded-container w-full border border-gray-300 p-2 text-lg"
		/>
		{#if $errors.name}
			<p class="text-sm text-red-500">{$errors.name}</p>
		{/if}
	</div>

	<div>
		<label for="description" class="block">Description *</label>
		<textarea
			id="description"
			name="description"
			bind:value={$form.description}
			class="rounded-container w-full border border-gray-300 p-2 text-lg"
		></textarea>
	</div>

	<div>
		<label for="deadline" class="block">Deadline</label>
		<input
			type="date"
			name="deadline"
			bind:value={$form.deadline}
			class="rounded-container w-full border border-gray-300 p-2 text-lg"
		/>
	</div>

	<div>
		<label for="defaultLanguage" class="block">Default Language *</label>
		<select
			id="defaultLanguage"
			name="defaultLanguage"
			bind:value={$form.defaultLanguage}
			aria-invalid={$errors.defaultLanguage ? 'true' : 'false'}
			required
			class="rounded-container w-full border border-gray-300 p-2 text-lg"
		>
			<option value="" disabled>Select a language</option>
			<option value="en">English</option>
			<option value="de">German</option>
			<option value="fr">French</option>
		</select>
		{#if $errors.defaultLanguage}
			<p class="text-sm text-red-500">{$errors.defaultLanguage}</p>
		{/if}
	</div>

	<div>
		<label for="context" class="block font-medium">Context ✨</label>
		<textarea
			id="context"
			name="additionalContext"
			bind:value={$form.additionalContext}
			class="rounded-container w-full border border-gray-300 p-3 text-base"
			placeholder="Provide additional context..."
		></textarea>
	</div>

	{#each $form.files as file (file.name)}
		<div
			class="rounded-container mt-2 flex items-center justify-between border border-gray-300 p-3"
		>
			<div class="flex items-center space-x-2">
				<svg
					class="h-6 w-6 text-gray-600"
					fill="none"
					stroke="currentColor"
					viewBox="0 0 24 24"
					xmlns="http://www.w3.org/2000/svg"
				>
					<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"
					></path>
				</svg>
				<span class="text-base text-gray-800">{file.name}</span>
			</div>
			<button
				type="button"
				onclick={() => removeFile()}
				class="rounded-container bg-red-500 px-3 py-1 text-white hover:bg-red-600"
			>
				Remove
			</button>
		</div>
	{/each}

	<FileUpload name="example-button" accept="image/*" onFileChange={console.log} maxFiles={2}>
		<button class="btn preset-filled">
			<IconUpload class="size-4" />
			<span>Select File</span>
		</button>
	</FileUpload>

	<p class="text-sm">*required fields</p>
	<SuperDebug data={$form} />
</form>
