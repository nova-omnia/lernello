<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { Plus, Trash2 } from 'lucide-svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { deleteLearningKit, getAllLearningKits } from '$lib/api/collections/learningKit';
	import { createMutation, createQuery, useQueryClient } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient.js';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';

	const client = useQueryClient();

	const kitsQuery = createQuery({
		queryKey: ['learning-kits-list'],
		queryFn: () => api(fetch).req(getAllLearningKits, null).parse()
	});
	const deleteKitMutation = createMutation({
		mutationKey: ['learning-kit', 'delete'],
		onSuccess: () => {
			client.invalidateQueries({ queryKey: ['learning-kits-list'] });
		},
		mutationFn: (kitId: string) => api(fetch).req(deleteLearningKit, null, kitId).parse()
	});

	interface Kit {
		uuid: string;
		name: string;
	}

	let showDeleteDialog = $state(false);
	let kitToDelete = $state<Kit | null>(null);

	async function handleConfirmDelete() {
		if (!kitToDelete) return;

		await $deleteKitMutation.mutateAsync(kitToDelete.uuid);

		showDeleteDialog = false;
		kitToDelete = null;
	}
</script>

<div class="p-5">
	<h1 class="h1">Dashboard</h1>
	<p class="preset-typo-subtitle">{$_('dashboard.welcome')}</p>

	{#if $kitsQuery.status === 'pending'}
		<div class="mt-5 flex flex-wrap gap-5">
			{#each Array(3) as _}
				<div class="card preset-filled-surface-100-900 grid w-52 gap-2 p-5">
					<div class="placeholder animate-pulse"></div>
					<div class="placeholder animate-pulse"></div>
					<div class="placeholder animate-pulse"></div>
				</div>
			{/each}
		</div>
	{:else if $kitsQuery.status === 'error'}
		<ErrorIllustration>{$_('learningKit.error.loadList')}</ErrorIllustration>
	{:else}
		<div class="mt-5 flex flex-wrap gap-5">
			<a
				href="/learningkit/create-form"
				class="border-surface-400-600 hover:bg-surface-100-900 relative flex w-52 flex-col items-center justify-center rounded-lg border border-dashed p-5 pt-10 text-center transition-colors"
			>
				<Plus class="text-primary-500 h-10 w-10" />
				<p>{$_('learningKit.create')}</p>
			</a>

			{#each $kitsQuery.data as kit (kit.uuid)}
				<a
					href={`/learningkit/${kit.uuid}`}
					class="border-surface-400-600 hover:bg-surface-100-900 relative w-52 rounded-lg border p-5 pt-10 text-center transition-colors"
				>
					<div class="absolute top-0 right-0 flex gap-2">
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
	{/if}
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
