<script lang="ts">
	import Sidebar from '$lib/components/Sidebar.svelte';
	import Topbar from '$lib/components/Topbar.svelte';

	let { children } = $props();
</script>

<Sidebar>
	<Topbar />
	<main class="relative min-h-0 flex-1 overflow-auto p-4">
		{@render children()}
	</main>
</Sidebar>
