<script lang="ts">
	import { MarkdownEditor } from 'carta-md';
	import { carta } from '$lib/components/blocks/carta';

	// Define the `data` prop with a default value
	export let data: { markdown: string; html: string } = {
		markdown: '',
		html: ''
	};
</script>

<div>
	<MarkdownEditor bind:value={data.markdown} mode="tabs" {carta} />
</div>
