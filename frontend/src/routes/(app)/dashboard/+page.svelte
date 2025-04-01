<script lang="ts">
	import { goto } from '$app/navigation';
	import { Pencil, Trash2 } from '@lucide/svelte';

	let { data } = $props();
	const kits = data.kits;

	function handleCreate() {
		goto('/learningkit/create-form');
	}

	function handleEdit(id: string) {
		goto(`/learningkit/create-form?edit=${id}`);
	}

	function handleDelete(id: string) {
		goto(`/learningkit/${id}`);
	}
</script>

<div class="p-5">
	<p class="mb-2.5 text-2xl font-bold">Dashboard</p>
	<div class="mb-2.5 text-xl">Good morning ...!</div>

	<a href="/learningkit" class="card preset-filled-surface-100-900 no-underline hover:underline">
		Manage Learning Kits
	</a>

	<div class="mt-5 flex flex-wrap gap-5">
		{#each kits as kit (kit.uuid)}
			<div
					on:click={() => goto(`/learningkit/${kit.uuid}`)}
					class="relative w-52 cursor-pointer rounded-lg border border-gray-300 p-5 pt-10 text-center hover:bg-gray-100 transition-colors"
			>
				<!-- Icons in top-right corner -->
				<div class="absolute right-2 top-2 flex gap-2">
					<!-- Edit icon -->
					<button
							on:click|stopPropagation={() => handleEdit(kit.uuid)}
							class="rounded p-1 text-blue-600 hover:bg-blue-100"
							aria-label="Edit"
					>
						<Pencil class="w-4 h-4" />
					</button>

					<!-- Delete icon -->
					<button
							on:click|stopPropagation={() => handleDelete(kit.uuid)}
							class="rounded p-1 text-red-600 hover:bg-red-100"
							aria-label="Delete"
					>
						<Trash2 class="w-4 h-4" />
					</button>
				</div>

				<!-- Card content -->
				<h3 class="my-2.5 font-semibold">{kit.name}</h3>
				<p class="text-gray-600">{kit.description}</p>
			</div>
		{/each}
	</div>

	<div class="mt-5">
		<button
				class="btn preset-filled-primary-400-600 cursor-pointer rounded border"
				on:click={handleCreate}
		>
			+ Create new Learning Kit
		</button>
	</div>
</div>
