<script lang="ts">
	import SuperDebug, { superForm } from 'sveltekit-superforms';
	import { toaster } from '$lib/states/toasterState.svelte.js';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation.js';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import { _ } from 'svelte-i18n';
	import { dev } from '$app/environment';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';
	import { error } from '@sveltejs/kit';

	let { data } = $props();

	const invalidate = useQueryInvalidation();

	const { form, errors, constraints, enhance } = superForm(data.form, {
		onResult() {
			invalidate(['latest-learning-kits-list']);
			invalidate(['all-learning-kits-list']);
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

{#if data.userInfo.role === INSTRUCTOR_ROLE}
	<PageContainer title={$_('learningKit.form.create.title')} centered={true}>
		<form method="POST" use:enhance action="?/create" class="space-y-4">
			<div class="space-y-6">
				<label class="label">
					<span class="label-text">{$_('common.name')} *</span>
					<input
						type="text"
						class="input preset-filled-surface-100-900"
						name="name"
						placeholder={$_('learningKit.name')}
						aria-invalid={$errors.name ? 'true' : undefined}
						bind:value={$form.name}
						{...$constraints.name}
					/>
					{#if $errors.name}
						<p class="text-error-500">{$errors.name}</p>
					{/if}
				</label>

				<label class="label">
					<span class="label-text">{$_('common.description')} <i>{$_('form.optional')}</i></span>
					<textarea
						name="description"
						bind:value={$form.description}
						class="textarea preset-filled-surface-100-900 min-h-24"
						placeholder={$_('common.descriptionPlaceholder')}
						aria-invalid={$errors.description ? 'true' : undefined}
						{...$constraints.description}
					></textarea>
					{#if $errors.description}
						<p class="text-error-500">{$errors.description}</p>
					{/if}
				</label>

				<label class="label">
					<span class="label-text">{$_('common.context')} <i>{$_('form.optional')}</i></span>
					<textarea
						name="context"
						bind:value={$form.context}
						class="textarea preset-filled-surface-100-900 min-h-24"
						placeholder={$_('common.additionalContext')}
						aria-invalid={$errors.context ? 'true' : undefined}
						{...$constraints.context}
					></textarea>
					{#if $errors.context}
						<p class="text-error-500">{$errors.context}</p>
					{/if}
				</label>

				<label class="label">
					<span class="label-text">{$_('form.deadlineLabel')} <i>{$_('form.optional')}</i></span>
					<input
						type="datetime-local"
						name="deadlineDate"
						bind:value={$form.deadlineDate}
						class="input preset-filled-surface-100-900"
						placeholder="YYYY-MM-DD"
						aria-invalid={$errors.deadlineDate ? 'true' : undefined}
						{...$constraints.deadlineDate}
					/>
					{#if $errors.deadlineDate}
						<p class="text-error-500">{$errors.deadlineDate}</p>
					{/if}
				</label>
			</div>
			<div class="flex justify-end gap-2">
				<a class="btn preset-outlined-surface-500" href="/dashboard">{$_('common.cancel')}</a>
				<button class="btn preset-filled-primary-500">{$_('learningKit.create')}</button>
			</div>
			<SuperDebug data={$form} display={dev} />
		</form>
	</PageContainer>
{:else}
	{error(403, 'You are not allowed to view this page')}
{/if}
