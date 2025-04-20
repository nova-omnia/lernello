<script lang="ts">
	import LanguageSelect from '$lib/components/LanguageSelect.svelte';
	import { _ } from 'svelte-i18n';
</script>

<nav class="preset-filled-surface-100-900 flex h-20 items-center justify-between px-5">
	<input
		class="input preset-filled-surface-50-950 h-10 w-sm"
		disabled
		type="search"
		placeholder={$_('app.searchPlaceholder')}
	/>
	<LanguageSelect />
</nav>
