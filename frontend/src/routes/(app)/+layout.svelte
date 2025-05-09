<script lang="ts">
	import Sidebar from '$lib/components/layout/Sidebar.svelte';
	import Topbar from '$lib/components/layout/Topbar.svelte';
	import { _ } from 'svelte-i18n';
	import { goto } from '$app/navigation';
	import { ChevronLeft } from 'lucide-svelte';
	import { onMount } from 'svelte';
	import { LoggedInUserNoRefreshSchema } from '$lib/schemas/response/LoggedInUser';

	function goBack() {
		if (history.length > 1) {
			history.back();
		} else {
			goto('/');
		}
	}

	let { children } = $props();

	function parseTokenFromLocalStorage() {
		const token = localStorage.getItem('lernello_auth_token') ?? '{}';
		const parsedToken = LoggedInUserNoRefreshSchema.parse(JSON.parse(token));

		const tokenExp = new Date(parsedToken.expires).getTime() - Date.now();
		const refreshTokenExp = new Date(parsedToken.refreshExpires).getTime() - Date.now();

		return {
			expiresMs: Math.max(tokenExp, 0),
			refreshExpiresMs: refreshTokenExp
		};
	}

	function queueRefresh() {
		const { expiresMs, refreshExpiresMs } = parseTokenFromLocalStorage();

		const autoLogoutTimer = setTimeout(() => {
			localStorage.removeItem('lernello_auth_token');
			window.location.reload();
		}, refreshExpiresMs);

		const refetchTimer = setTimeout(async () => {
			const data = await fetch('/refresh', {
				method: 'GET'
			});
			const json = await data.json();
			const loggedInUserNoRefresh = LoggedInUserNoRefreshSchema.parse(json);
			localStorage.setItem('lernello_auth_token', JSON.stringify(loggedInUserNoRefresh));

			clearInterval(autoLogoutTimer);
			queueRefresh();
		}, expiresMs / 2);

		function clearAllTimers() {
			clearTimeout(autoLogoutTimer);
			clearTimeout(refetchTimer);
		}
		return {
			clearAllTimers
		};
	}

	onMount(() => {
		const { clearAllTimers } = queueRefresh();
		return () => {
			clearAllTimers();
		};
	});
</script>

<div class="flex h-full">
	<Sidebar />
	<div class="flex flex-1 flex-col">
		<Topbar />
		<main class="relative min-h-0 flex-1 overflow-auto p-4">
			<div class="px-4 pt-4">
				<button class="flex items-center gap-1" onclick={goBack}>
					<ChevronLeft size={16} />
					{$_('common.return')}
				</button>
			</div>
			{@render children()}
		</main>
	</div>
</div>
