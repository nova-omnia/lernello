<script lang="ts">
	import SuperDebug, { superForm } from 'sveltekit-superforms';

	let { data } = $props();

	const { form, errors, constraints, message, enhance } = superForm(data.form);
</script>

<main class="flex h-full flex-col items-center justify-center">
	{#if $message}<h3>{$message}</h3>{/if}

	<form
		method="POST"
		use:enhance
		action="?/changePassword"
		class="card preset-filled-surface-100-900 border-surface-200-800 w-full max-w-lg space-y-8 border-[1px] p-8"
	>
		<h1 class="h2">Change your Password</h1>
		<p>Please change your generated password to your own password.</p>
		<div class="space-y-4">
			<label class="label">
				<span class="label-text">Old Password</span>
				<input
					class="input preset-filled-surface-200-800"
					name="oldPassword"
					type="password"
					placeholder="old password"
					aria-invalid={$errors.oldPassword ? 'true' : undefined}
					bind:value={$form.oldPassword}
					{...$constraints.oldPassword}
				/>
				{#if $errors.oldPassword}<span class="text-error-50-950">{$errors.oldPassword}</span>{/if}
			</label>
			<label class="label">
				<span class="label-text">New Password</span>
				<input
					class="input preset-filled-surface-200-800"
					name="newPassword"
					type="password"
					placeholder="new password"
					aria-invalid={$errors.newPassword ? 'true' : undefined}
					bind:value={$form.newPassword}
					{...$constraints.newPassword}
				/>
				{#if $errors.newPassword}<span class="text-error-50-950">{$errors.newPassword}</span>{/if}
			</label>
			<label class="label">
				<span class="label-text">Confirm Password</span>
				<input
					class="input preset-filled-surface-200-800"
					name="confirmPassword"
					type="password"
					placeholder="confirm password"
					aria-invalid={$errors.confirmPassword ? 'true' : undefined}
					bind:value={$form.confirmPassword}
					{...$constraints.confirmPassword}
				/>
				{#if $errors.confirmPassword}<span class="text-error-50-950">{$errors.confirmPassword}</span
					>{/if}
			</label>
		</div>
		<button class="btn preset-filled-primary-500 w-full">Change Password</button>
	</form>
	<SuperDebug data={$form} />
</main>
