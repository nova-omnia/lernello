<script lang="ts">
	import { sidebarState } from '$lib/components/sidebarState.svelte';
	import { Sidebar } from 'lucide-svelte';
</script>

<nav class="preset-filled-surface-100-900 flex pl-2">
	<button
		class="flex h-12 w-12 items-center justify-center rounded"
		onclick={sidebarState.toggleSidebar}
		aria-label="Toggle sidebar"
		aria-expanded={sidebarState.isExpanded}
		class:rotate-180={sidebarState.isExpanded}
	>
		<Sidebar size={24} />
	</button>
	<div
		class="input-group disabled ml-auto mr-2 grid-cols-[auto_1fr_auto] items-center justify-center"
	>
		<input class="ig-input preset-tonal rounded" type="search" placeholder="Search..." />
	</div>
</nav>
