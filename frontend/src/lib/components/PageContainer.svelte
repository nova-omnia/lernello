<script lang="ts">
	import type { Snippet } from 'svelte';

	interface BaseProps {
		title: string;
		subtitle?: string;
		children: Snippet<[]>;
	}

	const { title, subtitle, children }: BaseProps = $props();
</script>

<div class="space-y-4 p-4">
	<h1 class="preset-typo-headline">{title}</h1>
	{#if subtitle}
		<h2 class="preset-typo-subtitle">{subtitle}</h2>
	{/if}
	{@render children()}
</div>
