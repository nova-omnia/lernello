<script lang="ts">
	import AITheoryBlock from './../AddAITheoryBlockModal.svelte';
	import TextEditor from '$lib/components/MarkdownEditor/TextEditor.svelte';

	let content: string = '';
</script>

<TextEditor {content} />
<AITheoryBlock />
