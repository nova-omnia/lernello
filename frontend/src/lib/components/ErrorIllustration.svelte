<script lang="ts">
	import erroIllustration from '$lib/assets/error-illustration-sm.png';
	import type { Snippet } from 'svelte';
	import { _ } from 'svelte-i18n';

	interface ErrorIllustrationProps {
		center?: boolean;
		children: Snippet<[]>;
	}

	const { center = false, children }: ErrorIllustrationProps = $props();
</script>

<div
	class="card preset-filled-surface-100-900 my-4 grid max-w-max gap-2 p-4 text-center"
	class:mx-auto={center}
>
	<img alt={$_('errorIllustration.alt')} src={erroIllustration} width="150" class="mx-auto block" />
	<p class="text-2xl font-bold uppercase">{$_('errorIllustration.title')}</p>
	<p>{@render children()}</p>
</div>
