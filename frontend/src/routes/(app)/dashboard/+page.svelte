<script lang="ts">
	import { goto } from '$app/navigation';
	import { Pencil, Plus, Trash2 } from 'lucide-svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';

	type Kit = {
		uuid: string;
		name: string;
		description: string;
	};

	export let data: { kits: Kit[] };
	let kits = data.kits;

	let showDeleteDialog = false;
	let kitToDelete: Kit | null = null;

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
	<p class="mb-2.5 text-2xl font-bold">Dashboard</p>
	<div class="mb-2.5 text-xl">Good morning ...!</div>

	<div class="mt-5 flex flex-wrap gap-5">
		<div
			on:click={() => goto('/learningkit/create-form')}
			class="relative w-52 cursor-pointer rounded-lg border border-dashed border-gray-400 p-5 pt-10 text-center transition-colors hover:bg-gray-100 flex flex-col justify-center items-center"
		>
			<Plus class="h-10 w-10 text-primary-500" />
			<p class="text-gray-600">Create New Kit</p>
		</div>

		{#each kits as kit (kit.uuid)}
			<div
				on:click={() => goto(`/learningkit/${kit.uuid}`)}
				class="relative w-52 cursor-pointer rounded-lg border border-gray-300 p-5 pt-10 text-center transition-colors hover:bg-gray-100"
			>
				<div class="absolute top-2 right-2 flex gap-2">
					<a href="/learningkit/create-form?edit={kit.uuid}" on:click|stopPropagation>
						<Pencil class="h-4 w-4 text-primary-500 hover:text-blue-800" />
					</a>
					<button on:click={(e) => openDeleteDialog(kit, e)}>
						<Trash2 class="h-4 w-4 text-primary-500 hover:text-red-800" />
					</button>
				</div>

				<h3 class="my-2.5 font-semibold">{kit.name}</h3>
				<p class="text-gray-600">{kit.description}</p>
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
