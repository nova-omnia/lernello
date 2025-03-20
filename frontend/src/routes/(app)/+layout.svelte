<script lang="ts">
	import { writable } from 'svelte/store';

	//icons
	import {
		ChartLine,
		Files,
		MessageSquare,
		Settings,
		User,
		House,
		GraduationCap,
		ChevronRight
	} from 'lucide-svelte';

	let isExpanded = writable(false);
	let { children } = $props();

	function toggleSidebar() {
		isExpanded.update((value) => !value);
	}
</script>

<div class="flex h-screen w-full items-start justify-start space-x-6">
	<!-- Sidebar -->
	<div
		class="flex h-full flex-col items-center overflow-hidden rounded bg-gray-100 text-gray-700 transition-all duration-300"
		class:isFullWidth={$isExpanded}
		style="width: {$isExpanded ? '200px' : '4rem'}"
	>
		<!-- Expand/Collapse Button -->
		<button
			class="mt-3 flex h-12 w-12 items-center justify-center rounded text-gray-800"
			onclick={toggleSidebar}
			aria-label="Toggle sidebar"
			class:rotate-180={$isExpanded}
		>
			<ChevronRight size={24} />
		</button>
		<div>
			<!-- Logo -->
			<p class="mt-3 flex items-center" aria-label="Lernello">
				<GraduationCap size={24} />
				{#if $isExpanded}<span class="ml-3">Lernello</span>{/if}
			</p>
			<!-- Buttons Section -->
			<div class="mt-3 flex flex-col items-center border-t border-gray-300">
				<a class="mt-2 flex h-12 w-full items-center rounded" href="/home" aria-label="Home">
					<House size={24} />
					{#if $isExpanded}<span class="ml-3">Home</span>{/if}
				</a>
				<a
					class="mt-2 flex h-12 w-full items-center rounded"
					href="/learning-kits"
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
			<div class="mt-2 flex flex-col items-center border-t border-gray-300">
				<a
					class="mt-2 flex h-12 w-full items-center rounded"
					href="/settings"
					aria-label="Settings"
				>
					<Settings size={24} />
					{#if $isExpanded}<span class="ml-3">Settings</span>{/if}
				</a>
				<a
					class="relative mt-2 flex h-12 w-full items-center justify-center rounded"
					href="/notifications"
					aria-label="Notifications"
				>
					<MessageSquare size={24} />
					<span class="absolute top-0.5 left-2.5 mt-2 ml-2 h-2 w-2 rounded-full bg-indigo-500"
					></span>
					{#if $isExpanded}<span class="ml-3">Notifications</span>{/if}
				</a>
			</div>
		</div>
		<a
			class="mt-auto flex h-16 w-full items-center justify-center bg-gray-200"
			href="/profile"
			aria-label="Profile"
		>
			<User size={24} />
			{#if $isExpanded}<span class="ml-3">Profile</span>{/if}
		</a>
	</div>
	{@render children()}
</div>
