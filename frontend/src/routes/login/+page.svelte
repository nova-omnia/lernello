<script lang="ts">
	import { page } from '$app/state';
	import { _ } from 'svelte-i18n';
	import SuperDebug, { superForm } from 'sveltekit-superforms';
	import { toaster } from '$lib/states/toasterState.svelte.js';
	import { invalidateAll } from '$app/navigation';
	import { dev } from '$app/environment';

	let { data } = $props();

	const { form, errors, constraints, enhance } = superForm(data.form, {
		invalidateAll: false, // redirect inside load function would prevent onUpdated from being called
		async onUpdated({ form }) {
			if (form.valid && form.message) {
				localStorage.setItem('lernello_auth_token', JSON.stringify(form.message.tokenInfo));
				await invalidateAll(); // will call load function aka redirect
			}
		},
		onError: (error) => {
			console.error('Error:', error.result.error);
			toaster.create({
				description: $_('error.description', { values: { status: error.result.status } }),
				type: 'error'
			});
		}
	});
</script>

<main class="flex h-full flex-col items-center justify-center">
	<form
		method="POST"
		use:enhance
		action="{page.url.search ?? ''}{page.url.search ? '&' : '?'}/login"
		class="card preset-filled-surface-100-900 w-full max-w-lg space-y-8 p-8"
	>
		<h1 class="preset-typo-headline">{$_('login.title')}</h1>

		<div class="space-y-4">
			<label class="label">
				<span class="label-text">{$_('common.email')}</span>
				<input
					class="input preset-filled-surface-200-800"
					name="username"
					type="text"
					placeholder={$_('common.email')}
					aria-invalid={$errors.username ? 'true' : undefined}
					bind:value={$form.username}
					{...$constraints.username}
				/>
				{#if $errors.username}<span class="text-error-500">{$errors.username}</span>{/if}
			</label>
			<label class="label">
				<span class="label-text">{$_('form.passwordLabel')}</span>
				<input
					class="input preset-filled-surface-200-800"
					name="password"
					type="password"
					placeholder={$_('form.passwordPlaceholder')}
					aria-invalid={$errors.password ? 'true' : undefined}
					bind:value={$form.password}
					{...$constraints.password}
				/>
				{#if $errors.password}<p class="text-error-500">{$errors.password}</p>{/if}
			</label>
		</div>
		<button class="btn preset-filled-primary-500 w-full">{$_('login.signIn')}</button>
	</form>
	<SuperDebug data={$form} display={dev} />
</main>
