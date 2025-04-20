<script lang="ts">
	import type { Snippet } from 'svelte';

	interface BaseProps {
		title: string;
		children: Snippet<[]>;
	}

	const { title, children }: BaseProps = $props();
</script>

<div class="space-y-4 p-4">
	<h1 class="preset-typo-headline">{title}</h1>
	{@render children()}
</div>
