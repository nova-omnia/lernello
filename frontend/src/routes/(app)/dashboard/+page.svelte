<script lang="ts">
	import { goto } from '$app/navigation';
	import { Pencil, Trash2 } from '@lucide/svelte';

	let { data } = $props();
	const kits = data.kits;
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
				<div class="absolute right-2 top-2 flex gap-2">
					<a href="/learningkit/create-form?edit=${ kit.uuid }"><Pencil class="w-4 h-4" /></a>
					<a href="/dashboard"><Trash2 class="w-4 h-4" /></a>
				</div>

				<h3 class="my-2.5 font-semibold">{kit.name}</h3>
				<p class="text-gray-600">{kit.description}</p>
			</div>
		{/each}
	</div>

	<div class="mt-5">
		<a href="/learningkit/create-form" class="btn preset-filled-primary-400-600">Create new Learning Kit</a>
	</div>
</div>
