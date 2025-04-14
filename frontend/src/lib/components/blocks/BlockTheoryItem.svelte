<script lang="ts">
	import TextEditor from '$lib/components/MarkdownEditor/TextEditor.svelte';

	let content:string = '';
</script>

<!-- Add main-container class to the root div -->
<div class="bg-white dark:bg-gray-800 rounded-lg">
	<TextEditor {content}/>
</div>
