<script lang="ts">
	import LanguageSelect from '$lib/components/select/LanguageSelect.svelte';
	import { sidebarState } from '$lib/states/sidebarState.svelte';
	import { SidebarOpen } from 'lucide-svelte';
</script>

<nav class="preset-filled-surface-100-900 flex h-20 items-center justify-between px-5">
	<div class="flex items-center gap-4">
		{#if !sidebarState.isExpanded}
			<button
				class="flex h-12 w-12 items-center justify-center"
				onclick={sidebarState.toggleSidebar}
				aria-label="Toggle sidebar"
			>
				<SidebarOpen size={24} />
			</button>
		{/if}
	</div>
	<LanguageSelect />
</nav>
