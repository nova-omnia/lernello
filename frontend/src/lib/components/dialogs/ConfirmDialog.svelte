<script lang="ts">
	import { fade } from 'svelte/transition';

	export let isOpen = false;
	export let title = 'Confirm Action';
	export let message = 'Are you sure you want to proceed?';
	export let confirmText = 'Confirm';
	export let cancelText = 'Cancel';
	export let danger = false;

	export let onConfirm: () => void;
	export let onCancel: () => void;
</script>

{#if isOpen}
	<div
		transition:fade
		class="fixed inset-0 bg-black/30 flex items-center justify-center z-50"
	>
		<div class="bg-white rounded-lg p-6 w-full max-w-md">
			<h2 class="text-xl font-bold mb-4">{title}</h2>
			<p class="mb-6">{@html message}</p>

			<div class="flex justify-end gap-3">
				<button
					class="px-4 py-2 border rounded hover:bg-gray-100"
					on:click={onCancel}
				>
					{cancelText}
				</button>
				<button
					class="px-4 py-2 text-white rounded hover:bg-{danger ? 'red' : 'blue'}-700"
					class:bg-red-600={danger}
					class:bg-blue-600={!danger}
					on:click={onConfirm}
				>
					{confirmText}
				</button>
			</div>
		</div>
	</div>
{/if}

<svelte:options
	accessors={true}
	immutable={true}
/>