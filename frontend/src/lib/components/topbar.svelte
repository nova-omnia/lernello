<script>
	function toggleSidebar() {
		isExpanded.update((value) => !value);
	}
	const isExpanded = writable(false);

	import { Sidebar } from 'lucide-svelte';
</script>

<div class="top-bar flex">
	<button
		class="mt-3 flex h-12 w-12 items-center justify-center rounded"
		onclick={toggleSidebar}
		aria-label="Toggle sidebar"
		aria-expanded={$isExpanded}
		class:rotate-180={$isExpanded}
	>
		<Sidebar size={24} />
	</button>
</div>
