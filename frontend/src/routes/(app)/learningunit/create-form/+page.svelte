<script lang="ts">
	import { _ } from 'svelte-i18n';
	import SuperDebug, { superForm } from 'sveltekit-superforms';
	import { toaster } from '$lib/states/toasterState.svelte.js';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation.js';

	let { data } = $props();
	const invalidate = useQueryInvalidation();
	const { form, errors, constraints, enhance } = superForm(data.form, {
		onResult(event) {
			if (event.result.type === 'redirect') {
				invalidate(['learning-kit', $form.learningKitId]);
			}
		},
		onError(error) {
			console.error('Error:', error.result.error);
			toaster.create({
				title: $_('common.error.title'),
				description: $_('error.description', { values: { status: error.result.status } }),
				type: 'error'
			});
		}
	});
</script>

<form method="POST" use:enhance action="?/create" class="mx-auto max-w-lg space-y-4">
	<h1 class="h1">{$_('learningUnit.createTitle')}</h1>

	<label class="label">
		<span class="label-text">{$_('form.nameLabel')} </span>
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
			<p class="text-error-500 text-sm">{$errors.name}</p>
		{/if}
		<input type="hidden" name="learningKitId" bind:value={$form.learningKitId} />
	</label>

	<button class="btn preset-filled-primary-400-600 w-full">{$_('learningUnit.create')}</button>
	<SuperDebug data={$form} />
</form>
