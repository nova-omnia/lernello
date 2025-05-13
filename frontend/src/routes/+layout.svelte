<script lang="ts">
	import '../app.css';
	import { Toaster } from '@skeletonlabs/skeleton-svelte';
	import { toaster } from '$lib/states/toasterState.svelte';
	import { QueryClientProvider } from '@tanstack/svelte-query';
	import { SvelteQueryDevtools } from '@tanstack/svelte-query-devtools';

	let { children, data } = $props();
</script>

<QueryClientProvider client={data.queryClient}>
	{@render children()}
	<Toaster {toaster}></Toaster>
	<SvelteQueryDevtools />
</QueryClientProvider>
