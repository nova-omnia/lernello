<script lang="ts">
	import { goto } from '$app/navigation';
	import { Pencil, Plus, Trash2 } from 'lucide-svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';

	type Kit = {
		uuid: string;
		name: string;
		description: string;
	};

	const { data } = $props<{ data: { kits: Kit[] } }>();
	let kits = $state(data.kits);

	let showDeleteDialog = $state(false);
	let kitToDelete = $state<Kit | null>(null);

	function openDeleteDialog(kit: Kit, event: MouseEvent) {
		event.stopPropagation();
		kitToDelete = kit;
		showDeleteDialog = true;
	}

	function closeDeleteDialog() {
		showDeleteDialog = false;
		kitToDelete = null;
	}

	async function handleConfirmDelete() {
		if (!kitToDelete) return;

		const form = document.createElement('form');
		form.method = 'POST';
		form.action = '?/delete';

		const input = document.createElement('input');
		input.type = 'hidden';
		input.name = 'uuid';
		input.value = kitToDelete.uuid;

		form.appendChild(input);
		document.body.appendChild(form);
		form.submit();
	}
</script>

<div class="p-5">
	<p class="mb-2.5 text-2xl font-bold text-gray-900 dark:text-white">Dashboard</p>
	<div class="mb-2.5 text-xl text-gray-800 dark:text-gray-300">Good morning ...!</div>

	<div class="mt-5 flex flex-wrap gap-5">
		<div
			on:click={() => goto('/learningkit/create-form')}
			class="relative flex w-52 cursor-pointer flex-col items-center justify-center rounded-lg border border-dashed border-gray-400 p-5 pt-10 text-center transition-colors hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-800"
		>
			<Plus class="text-primary-500 h-10 w-10" />
			<p class="text-gray-600 dark:text-gray-300">Create New Kit</p>
		</div>

		{#each kits as kit (kit.uuid)}
			<div
				on:click={() => goto(`/learningkit/${kit.uuid}`)}
				class="relative w-52 cursor-pointer rounded-lg border border-gray-300 p-5 pt-10 text-center transition-colors hover:bg-gray-100 dark:border-gray-700 dark:hover:bg-gray-800"
			>
				<div class="absolute top-2 right-2 flex gap-2">
					<a href="/learningkit/create-form?edit={kit.uuid}" on:click|stopPropagation>
						<Pencil class="text-primary-500 h-4 w-4 hover:text-blue-800 dark:hover:text-blue-400" />
					</a>
					<button on:click={(e) => openDeleteDialog(kit, e)}>
						<Trash2 class="text-primary-500 h-4 w-4 hover:text-red-800 dark:hover:text-red-400" />
					</button>
				</div>

				<h3 class="my-2.5 font-semibold text-gray-900 dark:text-white">{kit.name}</h3>
				<p class="text-gray-600 dark:text-gray-300">{kit.description}</p>
			</div>
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
	onCancel={closeDeleteDialog}
/>
