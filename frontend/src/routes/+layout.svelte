<script lang="ts">
	import '../app.css';
	import { ToastProvider } from '@skeletonlabs/skeleton-svelte';
	import LoadingRing from '$lib/components/LoadingRing.svelte';
	import { onMount } from 'svelte';
	import { initI18n } from '$lib/i18n/i18n';

	let i18nReady = $state(false);
	const { data, children } = $props();

	onMount(async () => {
		await initI18n(data.locale);
		i18nReady = true;
	});
</script>

{#if !i18nReady}
	<LoadingRing />
{:else}
	<ToastProvider>
		{@render children()}
	</ToastProvider>
{/if}
