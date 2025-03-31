<script lang="ts">
	import '../app.css';
	import { ToastProvider } from '@skeletonlabs/skeleton-svelte';

	let { children } = $props();
</script>

<ToastProvider>
	{@render children()}
</ToastProvider>
