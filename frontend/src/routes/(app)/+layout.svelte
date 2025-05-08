<script lang="ts">
	import Sidebar from '$lib/components/layout/Sidebar.svelte';
	import Topbar from '$lib/components/layout/Topbar.svelte';
	import { _ } from 'svelte-i18n';
	import { goto } from '$app/navigation';
	import { ChevronLeft } from 'lucide-svelte';
	import { onMount } from 'svelte';
	import { LoggedInUserSchema } from '$lib/schemas/response/LoggedInUser';

	function goBack() {
		if (history.length > 1) {
			history.back();
		} else {
			goto('/');
		}
	}

	let { children } = $props();

	onMount(() => {
		const token = localStorage.getItem('lernello_auth_token') ?? '{}';
		try {
			const parsedToken = LoggedInUserSchema.parse(JSON.parse(token));
			const tokenExp = new Date(parsedToken.expires).getTime() - Date.now();
			const refreshTokenExp = new Date(parsedToken.refreshExpires).getTime() - Date.now();
			const createAutoLogoutTimer = () => {
				return setTimeout(() => {
					localStorage.removeItem('lernello_auth_token');
					goto('/login');
				}, refreshTokenExp);
			};
			let autoLogoutTimer = createAutoLogoutTimer();
			setTimeout(async () => {
				// refresh
				alert('TIME TO REFRESH THE TOKEN!');

				clearInterval(autoLogoutTimer);
				autoLogoutTimer = createAutoLogoutTimer();
			}, tokenExp / 2);
			console.log('Parsed token:', parsedToken);
			console.log('Token expires in:', tokenExp, 'ms');
			console.log('Refresh token expires in:', refreshTokenExp, 'ms');
		} catch (e) {
			console.error('Error parsing token:', e);
		}
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
