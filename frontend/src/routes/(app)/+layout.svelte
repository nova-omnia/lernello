<script lang="ts">
	import Sidebar from '$lib/components/Sidebar.svelte';
	import Topbar from "$lib/components/Topbar.svelte";

	let { children } = $props();
</script>

<div class="flex">
	<Sidebar />
	<div class="flex-1">
		<Topbar />
		{@render children()}
	</div>
</div>
