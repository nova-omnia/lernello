<script lang="ts">
	import '../app.css';
	import { Toaster } from '@skeletonlabs/skeleton-svelte';
	import { toaster } from '$lib/states/toasterState.svelte';
	import { QueryClientProvider } from '@tanstack/svelte-query';
	import { SvelteQueryDevtools } from '@tanstack/svelte-query-devtools';

	let props = $props();
</script>

<QueryClientProvider client={props.data.queryClient}>
	{@render props.children()}
	<Toaster {toaster}></Toaster>
	<SvelteQueryDevtools />
</QueryClientProvider>
