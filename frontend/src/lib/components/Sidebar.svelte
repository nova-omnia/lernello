<script lang="ts">
	import { Navigation } from '@skeletonlabs/skeleton-svelte';
	import logo from '$lib/assets/Lernello_Logo.svg';
	import { _ } from 'svelte-i18n';
	import { ChartLine, Folder, LayoutDashboard, LogOut, Settings, User } from 'lucide-svelte';

	const { children } = $props();
</script>

<div class="card grid h-screen w-full grid-cols-[auto_1fr]">
	<!-- Component -->
	<Navigation.Rail expanded={true} tilesClasses="!h-12 !min-h-12 !justify-start !gap-1">
		<!-- Header -->
		{#snippet header()}
			<div class="flex h-20 w-full items-center px-4">
				<img alt="Lernello" src={logo} width="36" class="object-cover object-center" />
				<p class="preset-typo-subtitle !text-black">{$_('app.companyName')}</p>
			</div>
		{/snippet}

		<!-- Main Tiles -->
		{#snippet tiles()}
			<p class="preset-typo-caption w-full px-4 pt-8">{$_('sidebar.general')}</p>
			<Navigation.Tile labelExpanded={$_('sidebar.dashboard')} href="/dashboard">
				<LayoutDashboard size={24} />
			</Navigation.Tile>
			<Navigation.Tile labelExpanded={$_('sidebar.folders')} href="/folders">
				<Folder size={24} />
			</Navigation.Tile>
			<Navigation.Tile labelExpanded={$_('sidebar.statistics')} href="/statistics">
				<ChartLine size={24} />
			</Navigation.Tile>
			<p class="preset-typo-caption w-full px-4 pt-8">{$_('sidebar.configuration')}</p>
			<Navigation.Tile labelExpanded={$_('sidebar.settings')} href="/settings">
				<Settings size={24} />
			</Navigation.Tile>
			<Navigation.Tile labelExpanded={$_('sidebar.profile')} href="/profile">
				<User size={24} />
			</Navigation.Tile>
		{/snippet}

		<!-- Footer -->
		{#snippet footer()}
			<Navigation.Tile labelExpanded={$_('sidebar.logout')} href="/logout">
				<LogOut size={24} />
			</Navigation.Tile>
		{/snippet}
	</Navigation.Rail>
	<!-- Content -->
	<div class="flex flex-1 flex-col">
		{@render children()}
	</div>
</div>
