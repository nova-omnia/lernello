<script lang="ts">
	import type { Snippet } from 'svelte';

	interface BaseProps {
		title: string;
		description?: string;
		subtitle?: string;
		centered?: boolean;
		children: Snippet<[]>;
	}

	const { title, description, subtitle, centered, children }: BaseProps = $props();
</script>

<div class="space-y-4 p-4" class:mx-auto={centered} class:max-w-2xl={centered}>
	<h1 class="preset-typo-headline">{title}</h1>
	{#if description}
		<p class="preset-typo-description">{description}</p>
	{/if}
	{#if subtitle}
		<h2 class="preset-typo-subtitle">{subtitle}</h2>
	{/if}
	{@render children()}
</div>
