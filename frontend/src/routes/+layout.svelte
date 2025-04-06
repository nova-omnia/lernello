<script lang="ts">
	import '../app.css';
	import { ToastProvider } from '@skeletonlabs/skeleton-svelte';

	const { children } = $props();
</script>

<ToastProvider>
	{@render children()}
</ToastProvider>
