<script lang="ts">
	import Sidebar from '$lib/components/Sidebar.svelte';
	import Topbar from '$lib/components/Topbar.svelte';

	let { children } = $props();
</script>

<div class="flex h-full">
	<Sidebar />
	<div class="flex flex-1 flex-col">
		<Topbar />
		<main class="min-h-0 flex-1 overflow-auto p-4">
			{@render children()}
		</main>
	</div>
</div>
