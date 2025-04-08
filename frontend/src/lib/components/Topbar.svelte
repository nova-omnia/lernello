<script lang="ts">
	import { sidebarState } from '$lib/states/sidebarState.svelte';
	import { SidebarOpen } from 'lucide-svelte';
	import { page } from '$app/state';
	import LanguageSelect from '$lib/components/LanguageSelect.svelte';
</script>

<nav class="preset-filled-surface-100-900 flex min-h-13 items-center justify-between px-5 py-1">
	{#if !sidebarState.isExpanded}
		<button
			class="flex h-12 w-12 items-center justify-center"
			onclick={sidebarState.toggleSidebar}
			aria-label="Toggle sidebar"
		>
			<SidebarOpen size={24} />
		</button>
	{/if}
	{page.url.pathname.at(1)?.toUpperCase() + page.url.pathname.slice(2)}
	<div
		class="input-group disabled mr-2 ml-auto grid-cols-[auto_1fr_auto] items-center justify-center"
	>
		<input class="ig-input preset-tonal rounded" type="search" placeholder="Search..." />
	</div>
	<LanguageSelect />
</nav>
