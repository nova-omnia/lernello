<script lang="ts">
	import LanguageSelect from '$lib/components/select/LanguageSelect.svelte';
	import { sidebarState } from '$lib/states/sidebarState.svelte';
	import { Moon, SidebarOpen, Sun } from 'lucide-svelte';

	let isDark = false;
	function toggleDarkMode() {
		isDark = !isDark;
		document.documentElement.classList.toggle('dark');
	}
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
	<div class="flex items-center gap-4">
		<button type="button" class="btn" onclick={toggleDarkMode}>
			{#if isDark}
				<Sun size={24} />
			{:else}
				<Moon size={24} />
			{/if}
		</button>
		<LanguageSelect />
	</div>
</nav>
