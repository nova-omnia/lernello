<script lang="ts">
	import SuperDebug, { superForm } from 'sveltekit-superforms';
	import { _ } from 'svelte-i18n';
	import { dev } from '$app/environment';

	let { data } = $props();

	const { form, errors, constraints, message, enhance } = superForm(data.form);
</script>

<main class="flex h-full flex-col items-center justify-center">
	{#if $message}<h3>{$message}</h3>{/if}

	<form
		method="POST"
		use:enhance
		action="?/changePassword"
		class="card preset-filled-surface-100-900 w-full max-w-lg space-y-8 p-8"
	>
		<h1 class="preset-typo-headline">{$_('changePassword.title')}</h1>
		<p>{$_('changePassword.description')}</p>
		<div class="space-y-4">
			<label class="label">
				<span class="label-text">{$_('form.newPasswordLabel')}</span>
				<input
					class="input preset-filled-surface-200-800"
					name="newPassword"
					type="password"
					placeholder={$_('form.newPasswordPlaceholder')}
					aria-invalid={$errors.newPassword ? 'true' : undefined}
					bind:value={$form.newPassword}
					{...$constraints.newPassword}
				/>
				{#if $errors.newPassword}
					<p class="text-error-500">{$errors.newPassword}</p>
				{/if}
			</label>
			<label class="label">
				<span class="label-text">{$_('form.confirmPasswordLabel')}</span>
				<input
					class="input preset-filled-surface-200-800"
					name="confirmPassword"
					type="password"
					placeholder={$_('form.confirmPasswordPlaceholder')}
					aria-invalid={$errors.confirmPassword ? 'true' : undefined}
					bind:value={$form.confirmPassword}
					{...$constraints.confirmPassword}
				/>
				{#if $errors.confirmPassword}
					<p class="text-error-500">{$errors.confirmPassword}</p>
				{/if}
			</label>
		</div>
		<button class="btn preset-filled-primary-500 w-full">
			{$_('changePassword.button')}
		</button>
	</form>
	<SuperDebug data={$form} display={dev} />
</main>
