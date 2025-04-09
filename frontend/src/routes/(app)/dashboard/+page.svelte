<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { goto } from '$app/navigation';
	import { Pencil, Plus, Trash2 } from 'lucide-svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { browserApiClient } from '$lib/api/browserApiClient';
	import { deleteLearningKit } from '$lib/api/collections/learningKit';
	import { invalidate } from '$app/navigation';

	interface Kit {
		uuid: string;
		name: string;
	}

	let { data } = $props();

	let showDeleteDialog = $state(false);
	let kitToDelete = $state<Kit | null>(null);

	async function handleConfirmDelete() {
		if (!kitToDelete) return;

		await browserApiClient.req(deleteLearningKit, null, kitToDelete.uuid);
		await invalidate('learningkits:list');

		showDeleteDialog = false;
		kitToDelete = null;
	}
</script>

<div class="p-5">
	<p class="mb-2.5 text-2xl font-bold text-gray-900 dark:text-white">{$_('dashboard.welcome')}</p>

	<div class="mt-5 flex flex-wrap gap-5">
		<a
			href="/learningkit/create-form"
			class="relative flex w-52 flex-col items-center justify-center rounded-lg border border-dashed border-gray-400 p-5 pt-10 text-center transition-colors hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-800"
		>
			<Plus class="text-primary-500 h-10 w-10" />
			<p class="text-gray-600 dark:text-gray-300">Create New Kit</p>
		</a>

		{#each data.kits as kit (kit.uuid)}
			<a
				href={`/learningkit/${kit.uuid}`}
				class="relative w-52 rounded-lg border border-gray-300 p-5 pt-10 text-center transition-colors hover:bg-gray-100 dark:border-gray-700 dark:hover:bg-gray-800"
			>
				<div class="absolute right-2 top-2 flex gap-2">
					<button onclick={() => goto(`/learningkit/create-form?edit=${kit.uuid}`)}>
						<Pencil class="text-primary-500 h-4 w-4 hover:text-blue-800 dark:hover:text-blue-400" />
					</button>
					<button
						onclick={(e) => {
							e.preventDefault();
							kitToDelete = kit;
							showDeleteDialog = true;
						}}
					>
						<Trash2 class="text-primary-500 h-4 w-4 hover:text-red-800 dark:hover:text-red-400" />
					</button>
				</div>

				<h3 class="my-2.5 font-semibold text-gray-900 dark:text-white">{kit.name}</h3>
				<p class="text-gray-600 dark:text-gray-300">{kit.description}</p>
			</a>
		{/each}
	</div>
</div>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title="Confirm Deletion"
	message={`Are you sure you want to delete "${kitToDelete?.name}"?`}
	confirmText="Delete"
	danger={true}
	onConfirm={handleConfirmDelete}
	onCancel={() => {
		showDeleteDialog = false;
		kitToDelete = null;
	}}
/>
