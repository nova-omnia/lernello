<script lang="ts">
	import { isExpanded } from '$lib/stores/sidebar';
	import { Sidebar } from 'lucide-svelte';

	function toggleSidebar() {
		isExpanded.update((value) => !value);
	}
</script>

<nav class="flex">
	<button
		class="flex h-12 w-12 items-center justify-center rounded"
		onclick={toggleSidebar}
		aria-label="Toggle sidebar"
		aria-expanded={$isExpanded}
		class:rotate-180={$isExpanded}
	>
		<Sidebar size={24} />
	</button>
</nav>
