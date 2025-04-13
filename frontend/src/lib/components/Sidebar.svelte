<script lang="ts">
	import logo from '$lib/assets/Lernello_Logo.svg';
	import { sidebarState } from '$lib/states/sidebarState.svelte';
	import {
		ChartLine,
		Folder,
		LayoutDashboard,
		LogOut,
		Settings,
		SidebarClose,
		User
	} from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
</script>

{#snippet sidebarItemLabel(label: string)}
	<span
		class="ml-3 text-nowrap transition-all"
		class:visible={sidebarState.isExpanded}
		class:w-max={sidebarState.isExpanded}
		class:opacity-100={sidebarState.isExpanded}
		class:invisible={!sidebarState.isExpanded}
		class:w-0={!sidebarState.isExpanded}
		class:opacity-0={!sidebarState.isExpanded}>{label}</span
	>
{/snippet}

<!-- Sidebar -->
<div
	class="preset-filled-surface-100-900 flex h-screen flex-col items-start justify-start overflow-hidden p-4 transition-all duration-300"
	class:w-64={sidebarState.isExpanded}
	class:w-16={!sidebarState.isExpanded}
>
	<div class="w-max min-w-full space-y-2">
		<!-- Logo -->
		<p class="flex items-center" aria-label="Lernello">
			<img alt="Lernello" src={logo} width="24" class="object-cover object-center" />
			{@render sidebarItemLabel('Lernello')}
			{#if sidebarState.isExpanded}
				<button
					class="ml-auto items-center justify-center"
					onclick={sidebarState.toggleSidebar}
					aria-label="Toggle sidebar"
					aria-expanded={sidebarState.isExpanded}
				>
					<SidebarClose size={24} />
				</button>
			{/if}
		</p>

		<hr class="hr" />
		<!-- Buttons Section -->
		<div class="flex flex-col items-center">
			<a class="flex w-full items-center py-2" href="/dashboard" aria-label="Dashboard">
				<LayoutDashboard size={24} />
				{@render sidebarItemLabel($_('sidebar.Dashboard'))}
			</a>
			<a class="flex w-full items-center py-2" href="/folders" aria-label="Folders">
				<Folder size={24} />
				{@render sidebarItemLabel($_('sidebar.Folders'))}
			</a>
			<a class="flex w-full items-center py-2" href="/statistics" aria-label="Statistics">
				<ChartLine size={24} />
				{@render sidebarItemLabel($_('sidebar.Statistics'))}
			</a>
		</div>

		<hr class="hr" />
	</div>

	<div class="mt-auto flex flex-col items-center">
		<a class="flex w-full items-center py-2" href="/settings" aria-label="Settings">
			<Settings size={24} />
			{@render sidebarItemLabel($_('sidebar.Settings'))}
		</a>
		<a class="flex w-full items-center py-2" href="/profile" aria-label="Profile">
			<User size={24} />
			{@render sidebarItemLabel($_('sidebar.Profile'))}
		</a>
		<form method="POST" action="/logout" class="flex w-full items-center py-2">
			<button class="flex" aria-label="Logout">
				<LogOut size={24} />
				{@render sidebarItemLabel($_('sidebar.Logout'))}
			</button>
		</form>
	</div>
</div>
