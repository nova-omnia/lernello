<svelte:options accessors={true} immutable={true} />

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
	<div transition:fade class="fixed inset-0 z-50 flex items-center justify-center bg-black/30">
		<div class="w-full max-w-md rounded-lg bg-white p-6">
			<h2 class="mb-4 text-xl font-bold">{title}</h2>
			<p class="mb-6">{@html message}</p>

			<div class="flex justify-end gap-3">
				<button class="rounded border px-4 py-2 hover:bg-gray-100" on:click={onCancel}>
					{cancelText}
				</button>
				<button
					class="rounded px-4 py-2 text-white hover:bg-{danger ? 'red' : 'blue'}-700"
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
