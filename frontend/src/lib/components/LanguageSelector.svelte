<script lang="ts">
	import DefaultSelector from './DefaultSelector.svelte';
	import { _, locale, locales } from 'svelte-i18n';
	import { browserApiClient } from '$lib/api/browserApiClient';
	import { setUserLocale } from '$lib/api/collections/userLocale';
	import type { ToastContext } from '@skeletonlabs/skeleton-svelte';
	import { getContext } from 'svelte';

	const toast: ToastContext = getContext('toast');

	function selectLocale(loc: string) {
		browserApiClient.req(setUserLocale, { locale: loc }).catch((error) => {
			console.error('Error:', error.result.error);
			toast.create({
				title: $_('error.title'),
				description: $_('error.description', { values: { error: error.result.error } }),
				type: 'error'
			});
		});

		locale.set(loc);
	}
</script>

<div class="relative inline-block w-40">
	<DefaultSelector options={$locales} selected={$locale} onSelect={selectLocale}>
		<span slot="selected">{$locale}</span>
		<span slot="option" let:option>{$_(`languageName.${option}`)}</span>
	</DefaultSelector>
</div>
