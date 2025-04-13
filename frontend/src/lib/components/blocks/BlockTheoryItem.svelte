<script lang="ts">
	import { MarkdownEditor } from 'carta-md';
	import { carta } from '$lib/components/blocks/carta';
	import { _ } from 'svelte-i18n'

	// Define the `data` prop with a default value
	export let data: { markdown: string; html: string } = {
		markdown: '',
		html: ''
	};
</script>

<div class="rounded border border-blue-300 bg-blue-100 p-4 shadow">
	<h2 class="text-xl font-bold text-blue-800">{$_('block.theoryBlock')}</h2>
	<MarkdownEditor bind:value={data.markdown} mode="tabs" {carta} />
</div>
