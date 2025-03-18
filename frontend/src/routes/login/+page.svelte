<script lang="ts">
	const { form } = $props();
</script>

<main class="flex h-full flex-col items-center justify-center">
	<!-- {#if form?.error}
		<div class="card preset-filled-error-100-900 border-surface-200-800 mb-4 p-4">
			<p class="error">{form.error}</p>
			<p class="error">{form.message}</p>
		</div>
	{/if} -->

	<form
		method="POST"
		action="?/loginUser"
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
				/>
			</label>
			<label class="label">
				<span class="label-text">Passwort</span>
				<input
					class="input preset-filled-surface-200-800"
					name="password"
					type="password"
					placeholder="password"
				/>
			</label>
		</div>
		<button class="btn preset-filled-primary-500 w-full">Sign in</button>
	</form>
</main>
