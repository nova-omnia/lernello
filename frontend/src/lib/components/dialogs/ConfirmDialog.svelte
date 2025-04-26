<script lang="ts">
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';

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
		title = $_('dialog.action.title'),
		message = $_('dialog.action.message'),
		confirmText = $_('common.confirm'),
		cancelText = $_('common.cancel'),
		danger = false,
		onConfirm,
		onCancel
	}: ConfirmDialogProps = $props();
</script>

<Modal
	open={isOpen}
	contentBase="card bg-surface-100-900 p-8 space-y-4 shadow-xl max-w-xl border-surface-500"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<h1 class="preset-typo-headline">{title}</h1>
		<p>{message}</p>

		<footer class="flex justify-end gap-3 pt-2">
			<button type="button" class="btn preset-tonal-surface" onclick={onCancel}>
				{cancelText}
			</button>
			<button
				type="button"
				class="btn preset-filled"
				class:preset-filled-error-500={danger}
				class:preset-filled-primary-500={!danger}
				onclick={onConfirm}
			>
				{confirmText}
			</button>
		</footer>
	{/snippet}
</Modal>
