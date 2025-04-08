<script lang="ts">
	import { _, locale, locales } from 'svelte-i18n';
	import { browserApiClient } from '$lib/api/browserApiClient';
	import { setUserLocale } from '$lib/api/collections/userLocale';
	import { toaster } from '$lib/states/toasterState.svelte';
	import Select from '$lib/components/Select.svelte';

	function selectLocale(loc: string) {
		browserApiClient
			.req(setUserLocale, { locale: loc })
			.then(() => locale.set(loc))
			.catch((error) => {
				console.error('Error:', error.result.error);
				toaster.create({
					title: $_('error.title'),
					description: $_('error.description', { values: { error: error.result.error } }),
					type: 'error'
				});
			});
	}
</script>

<div class="relative inline-block w-40">
	<Select selected={$locale} options={$locales} onSelect={selectLocale}>
		<span slot="selected">{$locale}</span>
		<span slot="option" let:option>{$_(`languageName.${option}`)}</span>
	</Select>
</div>
