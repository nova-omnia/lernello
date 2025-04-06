<script lang="ts">
	import { page } from '$app/state';
	import { superForm } from 'sveltekit-superforms';
	import SuperDebug from 'sveltekit-superforms';
	import { setAuthCookie } from '$lib/api/collections/auth.js';
	import { browserApiClient } from '$lib/api/browserApiClient.js';
	import { goto } from '$app/navigation';
	import { getToastContext } from '$lib/states/toastContext.svelte.js';
	const toast = getToastContext();

	let { data } = $props();

	let loading = $state(false);

	const { form, errors, constraints, message, enhance } = superForm(data.form, {
		onError: (error) => {
			console.error('Error:', error.result.error);
			toast.create({
				title: 'Error',
				description: `Uh oh, something went wrong. (${error.result.status})`,
				type: 'error'
			});
		}
	});

	$effect(() => {
		if ($message) {
			loading = true;
			browserApiClient
				.reqRaw(setAuthCookie, $message.user, {
					headers: {
						Authorization: `Bearer ${$message.user.token}`
					},
					credentials: 'include'
				})
				.then((response) => response.json())
				.then((response) => setAuthCookie.response.schema.parse(response))
				.then(() => {
					loading = false;
					toast.create({
						title: 'Success',
						description: 'Login successful!',
						type: 'success'
					});
					goto($message.redirectTo);
				})
				.catch(() => {
					loading = false;
					toast.create({
						title: 'Error',
						description: `Uh oh, something went wrong. (cookie)`,
						type: 'error'
					});
				});
		}
	});
</script>

<main class="flex h-full flex-col items-center justify-center">
	<form
		method="POST"
		use:enhance
		action="{page.url.search ?? ''}{page.url.search ? '&' : '?'}/login"
		class="card preset-filled-surface-100-900 border-surface-200-800 w-full max-w-lg space-y-8 border-[1px] p-8"
		class:opacity-50={loading}
		class:pointer-events-none={loading}
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
