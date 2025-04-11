<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { Plus, Trash2 } from 'lucide-svelte';
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
	<h1 class="h1">Dashboard</h1>
	<p class="preset-typo-subtitle">{$_('dashboard.welcome')}</p>

	<div class="mt-5 flex flex-wrap gap-5">
		<a
			href="/learningkit/create-form"
			class="border-surface-400-600 hover:bg-surface-100-900 relative flex w-52 flex-col items-center justify-center rounded-lg border border-dashed p-5 pt-10 text-center transition-colors"
		>
			<Plus class="text-primary-500 h-10 w-10" />
			<p>Create New Kit</p>
		</a>

		{#each data.kits as kit (kit.uuid)}
			<a
				href={`/learningkit/${kit.uuid}`}
				class="border-surface-400-600 hover:bg-surface-100-900 relative w-52 rounded-lg border p-5 pt-10 text-center transition-colors"
			>
				<div class="absolute right-0 top-0 flex gap-2">
					<button
						class="p-2"
						onclick={(e) => {
							e.preventDefault();
							kitToDelete = kit;
							showDeleteDialog = true;
						}}
					>
						<Trash2 class="text-primary-500 h-4 w-4" />
					</button>
				</div>

				<h3 class="my-2.5 font-semibold">{kit.name}</h3>
				<p>{kit.description}</p>
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
