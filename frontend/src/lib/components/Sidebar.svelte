<script lang="ts">
	import { isExpanded } from '$lib/stores/sidebar';
	import { ChartLine, Files, Settings, User, GraduationCap, LayoutDashboard } from 'lucide-svelte';
</script>

{#snippet sidebarItemLabel(label: string)}
	<span
		class="ml-3 text-nowrap transition-all"
		class:visible={$isExpanded}
		class:w-max={$isExpanded}
		class:opacity-100={$isExpanded}
		class:invisible={!$isExpanded}
		class:w-0={!$isExpanded}
		class:opacity-0={!$isExpanded}>{label}</span
	>
{/snippet}

<!-- Sidebar -->
<div
	class="preset-filled-surface-100-900 flex h-screen flex-col items-start justify-start overflow-hidden rounded-l-lg p-4 transition-all duration-300"
	class:w-64={$isExpanded}
	class:w-16={!$isExpanded}
>
	<div class="w-max min-w-full space-y-2">
		<!-- Logo -->
		<p class="flex items-center" aria-label="Lernello">
			<GraduationCap size={24} />
			{@render sidebarItemLabel('Lernello')}
		</p>

		<hr class="hr" />
		<!-- Buttons Section -->
		<div class="flex flex-col items-center">
			<a class="flex w-full items-center rounded py-2" href="/dashboard" aria-label="Dashboard">
				<LayoutDashboard size={24} />
				{@render sidebarItemLabel('Dashboard')}
			</a>
			<a
				class="flex w-full items-center rounded py-2"
				href="/learningkit"
				aria-label="Learning Kit"
			>
				<Files size={24} />
				{@render sidebarItemLabel('Learning Kits')}
			</a>
			<a class="flex w-full items-center rounded py-2" href="/statistics" aria-label="Statistics">
				<ChartLine size={24} />
				{@render sidebarItemLabel('Statistics')}
			</a>
		</div>

		<hr class="hr" />
		<div class="flex flex-col items-center">
			<a class="flex w-full items-center rounded py-2" href="/settings" aria-label="Settings">
				<Settings size={24} />
				{@render sidebarItemLabel('Settings')}
			</a>
		</div>
	</div>

	<a class="mt-auto flex w-full items-center justify-start" href="/profile" aria-label="Profile">
		<User size={24} />
		{@render sidebarItemLabel('Profile')}
	</a>
</div>
