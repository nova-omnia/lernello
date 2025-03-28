<script lang="ts">
	import { superForm } from 'sveltekit-superforms';
	import SuperDebug from 'sveltekit-superforms';
	import CreateLearningKit from '../createLearningKit.svelte';

	let { data } = $props();
	const { form, errors, constraints, message, enhance } = superForm(data.form);
</script>

<CreateLearningKit {data} />

<main class="flex h-full flex-col items-center justify-center">
	{#if $message}<h3>{$message}</h3>{/if}

	<form
		method="POST"
		use:enhance
		action="?/login"
		class="card preset-filled-surface-100-900 border-surface-200-800 w-full max-w-lg space-y-8 border-[1px] p-8"
	>
		<h1 class="h2">Login</h1>
		<div class="space-y-4">
			<label class="label">
				<span class="label-text">Email</span>
				<input
					class="input preset-filled-surface-200-800"
					name="username"
					type="text"
					placeholder="email"
					aria-invalid={$errors.username ? 'true' : undefined}
					bind:value={$form.username}
					{...$constraints.username}
				/>
				{#if $errors.username}<span class="text-error-50-950">{$errors.username}</span>{/if}
			</label>
			<label class="label">
				<span class="label-text">Passwort</span>
				<input
					class="input preset-filled-surface-200-800"
					name="password"
					type="password"
					placeholder="password"
					aria-invalid={$errors.password ? 'true' : undefined}
					bind:value={$form.password}
					{...$constraints.password}
				/>
				{#if $errors.password}<span class="text-error-50-950">{$errors.password}</span>{/if}
			</label>
		</div>
		<button class="btn preset-filled-primary-500 w-full">Sign in</button>
	</form>
	<SuperDebug data={$form} />
</main>
