<script lang="ts">
	import AITheoryBlock from './../AITheoryBlock.svelte';
	import TextEditor from '$lib/components/MarkdownEditor/TextEditor.svelte';

	let content: string = '';
</script>

<!-- Add main-container class to the root div -->
<div class="rounded-lg bg-white dark:bg-gray-800">
	<TextEditor {content} />
	<AITheoryBlock/>
</div>
