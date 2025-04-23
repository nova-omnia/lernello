<script lang="ts">
	import { sidebarState } from '$lib/states/sidebarState.svelte';
	import { SidebarOpen } from 'lucide-svelte';
	import { page } from '$app/state';
	import LanguageSelect from '$lib/components/LanguageSelect.svelte';
</script>

<nav class="preset-filled-surface-100-900 flex min-h-13 items-center justify-between px-5 py-1">
	<div class="flex items-center gap-2">
		{#if !sidebarState.isExpanded}
			<button
				class="flex items-center justify-center"
				onclick={sidebarState.toggleSidebar}
				aria-label="Toggle sidebar"
			>
				<SidebarOpen size={24} />
			</button>
		{/if}

		<div class="text-left">
			{page.url.pathname.at(1)?.toUpperCase() + page.url.pathname.slice(2)}
		</div>
	</div>
	<LanguageSelect />
</nav>
