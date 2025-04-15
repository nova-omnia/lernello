<script lang="ts">
	import SuperDebug, { superForm } from 'sveltekit-superforms';
	import { _ } from 'svelte-i18n';

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
		<div class="space-y-2">
			<h2 class="h2">{$_('changePassword.title')}</h2>
			<p>{$_('changePassword.description')}</p>
		</div>
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
					<span class="text-error-500">{$errors.newPassword}</span>
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
					<span class="text-error-500">{$errors.confirmPassword}</span>
				{/if}
			</label>
		</div>
		<button class="btn preset-filled-primary-500 w-full">
			{$_('changePassword.button')}
		</button>
	</form>
	<SuperDebug data={$form} />
</main>
