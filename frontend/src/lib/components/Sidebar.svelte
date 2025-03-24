<script lang="ts">
	import { isExpanded } from '$lib/stores/sidebar';
	import { ChartLine, Files, Settings, User, GraduationCap, LayoutDashboard } from 'lucide-svelte';
</script>

<!-- Sidebar -->
<div
	class="preset-filled-primary-100-900 flex h-screen w-full flex-col items-center justify-start overflow-hidden rounded transition-all duration-300"
	class:isFullWidth={$isExpanded}
	style="width: {$isExpanded ? '200px' : '4rem'}"
>
	<div>
		<!-- Logo -->
		<p class="mt-3 flex items-center" aria-label="Lernello">
			<GraduationCap size={24} />
			{#if $isExpanded}<span class="ml-3">Lernello</span>{/if}
		</p>

		<!-- Buttons Section -->
		<div class="mt-3 flex flex-col items-center border-t">
			<a
				class="mt-2 flex h-12 w-full items-center rounded"
				href="/dashboard"
				aria-label="Dashboard"
			>
				<LayoutDashboard size={24} />
				{#if $isExpanded}<span class="ml-3">Dashboard</span>{/if}
			</a>
			<a
				class="mt-2 flex h-12 w-full items-center rounded"
				href="/learningkit"
				aria-label="Learning Kit"
			>
				<Files size={24} />
				{#if $isExpanded}<span class="ml-3">Learning Kits</span>{/if}
			</a>
			<a
				class="mt-2 flex h-12 w-full items-center rounded"
				href="/statistics"
				aria-label="Statistics"
			>
				<ChartLine size={24} />
				{#if $isExpanded}<span class="ml-3">Statistics</span>{/if}
			</a>
		</div>

		<div class="mt-2 flex flex-col items-center border-t">
			<a class="mt-2 flex h-12 w-full items-center rounded" href="/settings" aria-label="Settings">
				<Settings size={24} />
				{#if $isExpanded}<span class="ml-3">Settings</span>{/if}
			</a>
		</div>
	</div>

	<a
		class="mt-auto flex h-16 w-full items-center justify-center"
		href="/profile"
		aria-label="Profile"
	>
		<User size={24} />
		{#if $isExpanded}<span class="ml-3">Profile</span>{/if}
	</a>
</div>
