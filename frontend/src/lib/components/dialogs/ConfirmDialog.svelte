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
	contentBase="card bg-surface-100-900 p-4 space-y-4 shadow-xl max-w-screen-sm"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<header class="flex items-center justify-between">
			<h2 class="h2">{title}</h2>
		</header>

		<article>
			<p>{message}</p>
		</article>

		<footer class="flex justify-end gap-3 pt-2">
			<button type="button" class="btn preset-tonal" onclick={onCancel}>
				{cancelText}
			</button>

			<button
				type="button"
				class="btn"
				class:preset-tonal-error={danger}
				class:preset-filled={!danger}
				onclick={onConfirm}
			>
				{confirmText}
			</button>
		</footer>
	{/snippet}
</Modal>
