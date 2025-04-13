<script lang="ts">
	import { _, locale, locales } from 'svelte-i18n';
	import { browserApiClient } from '$lib/api/browserApiClient';
	import { toaster } from '$lib/states/toasterState.svelte';
	import Select from '$lib/components/Select.svelte';
	import { setUserLocale } from '$lib/api/collections/user';

	const selectLocale = async (loc: string) => {
		try {
			await browserApiClient.req(setUserLocale, { locale: loc });
			locale.set(loc);
		} catch (error) {
			console.error('Error:', error);
			toaster.create({
				title: $_('error.title'),
				description: $_('error.description', { values: { status: 'unknown' } }),
				type: 'error'
			});
		}
	};

	const localeNames = new Map<string, string>([
		['en', 'English'],
		['de', 'Deutsch'],
		['fr', 'Français'],
		['it', 'Italiano']
	]);
</script>

<div class="relative inline-block w-40">
	<Select
		selected={$locale}
		options={$locales.map((loc) => ({ value: loc, label: localeNames.get(loc) || loc }))}
		onSelect={selectLocale}
	/>
</div>
