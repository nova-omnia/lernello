<script lang="ts">
	import '../app.css';
	import { Toaster } from '@skeletonlabs/skeleton-svelte';
	import { toaster } from '$lib/states/toasterState.svelte.js';
	import Spinner from '$lib/components/Spinner.svelte';
	import { onMount } from 'svelte';
	import { initI18n } from '$lib/i18n/i18n';

	let i18nReady = $state(false);
	const { data, children } = $props();

	onMount(async () => {
		await initI18n(data.userInfo);
		i18nReady = true;
	});
</script>

{#if !i18nReady}
	<Spinner />
{:else}
	{@render children()}
{/if}
<Toaster {toaster}></Toaster>
