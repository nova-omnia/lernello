<script lang="ts">
	import logo from '$lib/assets/Lernello_Logo.svg';
	import {
		Boxes,
		ChartLine,
		Files,
		type IconProps,
		LayoutDashboard,
		LogOut,
		SidebarClose,
		Users
	} from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
	import { sidebarState } from '$lib/states/sidebarState.svelte';
	import { INSTRUCTOR_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';
	import type { SvelteComponent } from 'svelte';

	interface SidebarProps {
		role: RoleType;
	}
	const { role }: SidebarProps = $props();

	const iconSize = 24;
</script>

{#snippet sidebarItemStyle(label: string)}
	<span
		class:ml-3={sidebarState.isExpanded}
		class:w-max={sidebarState.isExpanded}
		class:opacity-100={sidebarState.isExpanded}
		class:hidden={!sidebarState.isExpanded}
	>
		{label}
	</span>
{/snippet}

{#snippet sidebarItem(label: string, href: string, Icon: typeof SvelteComponent<IconProps>)}
	<a
		class="card hover:preset-filled-surface-50-950 flex w-full items-center text-nowrap p-4"
		{href}
		aria-label={label}
	>
		<Icon size={iconSize} />
		{@render sidebarItemStyle(label)}
	</a>
{/snippet}

<!-- Sidebar -->
<div
	class="preset-filled-surface-100-900 flex h-screen flex-col items-start justify-start overflow-hidden px-4"
	class:w-64={sidebarState.isExpanded}
	class:w-fit={!sidebarState.isExpanded}
>
	<!-- Logo Section -->
	<div class="flex h-20 w-full items-center px-4">
		<img alt="Lernello" src={logo} width="24" class="object-cover object-center dark:invert" />
		{@render sidebarItemStyle($_('app.companyName'))}
		{#if sidebarState.isExpanded}
			<button
				class="ml-auto items-center justify-center"
				onclick={sidebarState.toggleSidebar}
				aria-label={$_('sidebar.toggleSidebar')}
				aria-expanded={sidebarState.isExpanded}
			>
				<SidebarClose size={24} />
			</button>
		{/if}
	</div>
	<hr class="hr" />

	<!-- Buttons Section -->
	<div class="flex w-full flex-col items-center gap-1">
		{#if sidebarState.isExpanded}
			<p class="preset-typo-caption w-full px-4 pt-8">{$_('sidebar.general')}</p>
		{:else}
			<p class="preset-typo-caption invisible w-full px-4 pt-8">_</p>
		{/if}
		{@render sidebarItem($_('sidebar.dashboard'), '/dashboard', LayoutDashboard)}
		{@render sidebarItem($_('learningKits.title'), '/learningkits', Boxes)}
		
		{#if role === INSTRUCTOR_ROLE}
			{@render sidebarItem($_('sidebar.statistics'), '/statistics', ChartLine)}
			{#if sidebarState.isExpanded}
				<p class="preset-typo-caption w-full px-4 pt-8">{$_('sidebar.configuration')}</p>
			{:else}
				<p class="preset-typo-caption invisible w-full px-4 pt-8">_</p>
			{/if}
			{@render sidebarItem($_('sidebar.users'), '/users', Users)}
			{@render sidebarItem($_('common.files'), '/files', Files)}
		{/if}
	</div>

	<div class="mt-auto flex w-full flex-col items-center gap-1 pb-4">
		<hr class="hr" />
		<form
			method="POST"
			action="/logout"
			class="card hover:preset-filled-surface-50-950 flex w-full items-center"
		>
			<button class="flex w-full p-4" aria-label={$_('sidebar.logout')}>
				<LogOut size={iconSize} />
				{@render sidebarItemStyle($_('sidebar.logout'))}
			</button>
		</form>
	</div>
</div>
