<script lang="ts">
	import TextEditor from '$lib/components/MarkdownEditor/TextEditor.svelte';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';

	const { block } = $props();
	let lastContent = block.content;

	function handleContentUpdate(newContent: string) {
		if (newContent !== lastContent) {
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				content: newContent
			});
			lastContent = newContent;
		}
	}
</script>

<TextEditor content={block.content} onUpdate={handleContentUpdate} />
