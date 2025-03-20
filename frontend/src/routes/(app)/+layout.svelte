<script lang="ts">
	import Sidebar from '$lib/components/sidebar.svelte';
	import Topbar from '$lib/components/topbar.svelte';

	let { children } = $props();
</script>

<div class="flex">
	<Sidebar />
	<div class="flex-1 pl-2">
		<Topbar />
		{@render children()}
	</div>
</div>
