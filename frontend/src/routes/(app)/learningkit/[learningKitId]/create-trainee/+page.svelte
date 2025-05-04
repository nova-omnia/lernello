<script lang="ts">
	import SuperDebug, { superForm } from 'sveltekit-superforms';
	import { toaster } from '$lib/states/toasterState.svelte.js';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation.js';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import { _ } from 'svelte-i18n';
	import { page } from '$app/state';

	const learningKitId = page.params.learningKitId;
	let { data } = $props();
	const invalidate = useQueryInvalidation();

	const { form, errors, constraints, enhance } = superForm(data.form, {
		onResult() {
			invalidate(['learning-kit', learningKitId]);
			invalidate(['trainees-list']);
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

<PageContainer title={$_('trainee.form.title')} centered={true}>
	<form method="POST" use:enhance action="?/create" class="space-y-4">
		<div class="space-y-6">
			<label class="label">
				<span class="label-text">{$_('common.email')} *</span>
				<input
					type="email"
					class="input preset-filled-surface-100-900"
					name="username"
					placeholder={$_('common.email')}
					aria-invalid={$errors.username ? 'true' : undefined}
					bind:value={$form.username}
					{...$constraints.username}
				/>
				{#if $errors.username}
					<p class="text-error-500">{$errors.username}</p>
				{/if}
			</label>

			<label class="label">
				<span class="label-text">{$_('common.name')} *</span>
				<input
					type="text"
					class="input preset-filled-surface-100-900"
					name="name"
					placeholder={$_('common.name')}
					aria-invalid={$errors.name ? 'true' : undefined}
					bind:value={$form.name}
					{...$constraints.name}
				/>
				{#if $errors.name}
					<p class="text-error-500">{$errors.name}</p>
				{/if}
			</label>

			<label class="label">
				<span class="label-text">{$_('common.surname')} *</span>
				<input
					type="text"
					class="input preset-filled-surface-100-900"
					name="surname"
					placeholder={$_('common.surname')}
					aria-invalid={$errors.surname ? 'true' : undefined}
					bind:value={$form.surname}
					{...$constraints.surname}
				/>
				{#if $errors.surname}
					<p class="text-error-500">{$errors.surname}</p>
				{/if}
			</label>
		</div>
		<div class="flex justify-end gap-2">
			<a class="btn preset-outlined-surface-500" href="/learningkit/{learningKitId}?tab=trainees">
				{$_('common.cancel')}
			</a>
			<button class="btn preset-filled-primary-500">{$_('createTrainee')}</button>
		</div>
		<SuperDebug data={$form} />
	</form>
</PageContainer>
