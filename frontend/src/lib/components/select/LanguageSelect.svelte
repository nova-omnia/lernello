<script lang="ts">
	import { _, locale, locales } from 'svelte-i18n';
	import { toaster } from '$lib/states/toasterState.svelte';
	import Select from '$lib/components/select/Select.svelte';
	import { setUserLocale } from '$lib/api/collections/user';
	import { api } from '$lib/api/apiClient';

	const selectLocale = async (loc: string) => {
		try {
			await api(fetch).req(setUserLocale, { locale: loc }).parse();
			locale.set(loc);
		} catch (error) {
			console.error('Error:', error);
			toaster.create({
				title: $_('common.error.title'),
				description: $_('error.description', { values: { status: 'unknown' } }),
				type: 'error'
			});
		}
	};

	$: localeNames = new Map<string, string>([
		['en', $_('common.english')],
		['de', $_('common.german')],
		['fr', $_('common.french')],
		['it', $_('common.italian')]
	]);
</script>

<div class="relative inline-block w-40">
	<Select
		selected={$locale}
		options={$locales.map((loc) => ({ value: loc, label: localeNames.get(loc) || loc }))}
		onSelect={selectLocale}
	/>
</div>
