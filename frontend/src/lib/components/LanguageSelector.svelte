<script lang="ts">
	import DefaultSelector from './DefaultSelector.svelte';
	import { _, locale, locales } from 'svelte-i18n';

	function selectLocale(loc: string) {
		locale.set(loc);
		localStorage.setItem('locale', loc);
	}
</script>

<div class="relative inline-block w-40">
	<DefaultSelector options={$locales} selected={$locale} onSelect={selectLocale}>
		<span slot="selected">{$_(`languageName.${$locale}`)}</span>
		<span slot="option" let:option>{$_(`languageName.${option}`)}</span>
	</DefaultSelector>
</div>
