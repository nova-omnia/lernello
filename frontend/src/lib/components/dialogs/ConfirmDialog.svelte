<script lang="ts">
	import { Modal } from '@skeletonlabs/skeleton-svelte';

	export let isOpen = false;
	export let title = 'Confirm Action';
	export let message = 'Are you sure you want to proceed?';
	export let confirmText = 'Confirm';
	export let cancelText = 'Cancel';
	export let danger = false;

	export let onConfirm: () => void;
	export let onCancel: () => void;

	function handleOpenChange(e: CustomEvent<{ open: boolean }>) {
		if (!e.detail.open) onCancel();
	}
</script>

<Modal
	open={isOpen}
	on:openChange={handleOpenChange}
	contentBase="bg-white dark:bg-gray-900 p-6 rounded-lg shadow-xl w-full max-w-md space-y-4"
	backdropClasses="bg-black/30 backdrop-blur-sm z-50"
>
	{#snippet content()}
	<header class="flex justify-between items-center">
		<h2 class="text-xl font-bold text-gray-900 dark:text-white">{title}</h2>
	</header>

	<article>
		<p class="text-gray-700 dark:text-gray-300">{message}</p>
	</article>

	<footer class="flex justify-end gap-3 pt-2">
		<button
			class="rounded border border-gray-300 dark:border-gray-600 px-4 py-2 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800"
			on:click={onCancel}
		>
			{cancelText}
		</button>

		<button
			class="rounded px-4 py-2 text-white"
			class:bg-red-600={danger}
			class:bg-blue-600={!danger}
			class:hover:bg-red-700={danger}
			class:hover:bg-blue-700={!danger}
			on:click={onConfirm}
		>
			{confirmText}
		</button>
	</footer>
	{/snippet}
</Modal>
