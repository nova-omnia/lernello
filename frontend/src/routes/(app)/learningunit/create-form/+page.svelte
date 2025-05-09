<script lang="ts">
	import { _ } from 'svelte-i18n';
	import SuperDebug, { superForm } from 'sveltekit-superforms';
	import { toaster } from '$lib/states/toasterState.svelte.js';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation.js';
	import { dev } from '$app/environment';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';

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

<PageContainer title={$_('learningUnit.createTitle')} centered={true}>
	<form method="POST" use:enhance action="?/create" class="space-y-4">
		<div class="space-y-6">
			<label class="label">
				<span class="label-text">{$_('form.nameLabel')}</span>
				<input
					type="text"
					class="input"
					name="name"
					placeholder={$_('form.nameLabel')}
					aria-invalid={$errors.name ? 'true' : undefined}
					bind:value={$form.name}
					{...$constraints.name}
				/>
				{#if $errors.name}
					<p class="text-error-500">{$errors.name}</p>
				{/if}
				<input type="hidden" name="learningKitId" bind:value={$form.learningKitId} />
			</label>
		</div>
		<div class="flex justify-end gap-2">
			<a
				class="btn preset-filled-error-500"
				href="/learningkit/{$form.learningKitId}?tab=learningUnits"
			>
				{$_('common.cancel')}
			</a>
			<button class="btn preset-filled-primary-500">{$_('learningUnit.create')}</button>
		</div>
		<SuperDebug data={$form} display={dev} />
	</form>
</PageContainer>
