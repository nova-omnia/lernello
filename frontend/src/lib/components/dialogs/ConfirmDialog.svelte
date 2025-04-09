<script lang="ts">
	import { Modal } from '@skeletonlabs/skeleton-svelte';

	interface ConfirmDialogProps {
		isOpen?: boolean;
		title?: string;
		message?: string;
		confirmText?: string;
		cancelText?: string;
		danger?: boolean;
		onConfirm: () => void;
		onCancel: () => void;
	}

	const {
		isOpen = false,
		title = 'Confirm Action',
		message = 'Are you sure you want to proceed?',
		confirmText = 'Confirm',
		cancelText = 'Cancel',
		danger = false,
		onConfirm,
		onCancel
	}: ConfirmDialogProps = $props();
</script>

<Modal
	open={isOpen}
	contentBase="bg-white dark:bg-gray-900 p-6 rounded-lg shadow-xl w-full max-w-md space-y-4"
	backdropClasses="bg-black/30 backdrop-blur-sm z-50"
>
	{#snippet content()}
		<header class="flex items-center justify-between">
			<h2 class="text-xl font-bold text-gray-900 dark:text-white">{title}</h2>
		</header>

		<article>
			<p class="text-gray-700 dark:text-gray-300">{message}</p>
		</article>

		<footer class="flex justify-end gap-3 pt-2">
			<button
				class="rounded border border-gray-300 px-4 py-2 text-gray-700 hover:bg-gray-100 dark:border-gray-600 dark:text-gray-300 dark:hover:bg-gray-800"
				onclick={onCancel}
			>
				{cancelText}
			</button>

			<button
				class="rounded px-4 py-2 text-white"
				class:bg-red-600={danger}
				class:bg-blue-600={!danger}
				class:hover:bg-red-700={danger}
				class:hover:bg-blue-700={!danger}
				onclick={onConfirm}
			>
				{confirmText}
			</button>
		</footer>
	{/snippet}
</Modal>
