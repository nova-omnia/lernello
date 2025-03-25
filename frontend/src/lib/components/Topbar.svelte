<script lang="ts">
	import { sidebarState } from '$lib/components/sidebarState.svelte';
	import { SidebarOpen } from 'lucide-svelte';
</script>

<nav class="preset-filled-surface-100-900 flex min-h-13 pt-1 pl-2">
	{#if !sidebarState.isExpanded}
		<button
			class="flex h-12 w-12 items-center justify-center"
			onclick={sidebarState.toggleSidebar}
			aria-label="Toggle sidebar"
		>
			<SidebarOpen size={24} />
		</button>
	{/if}
	<div
		class="input-group disabled mr-2 ml-auto grid-cols-[auto_1fr_auto] items-center justify-center"
	>
		<input class="ig-input preset-tonal rounded" type="search" placeholder="Search..." />
	</div>
</nav>
