<script lang="ts">
	import TextEditor from '$lib/components/MarkdownEditor/TextEditor.svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';

	interface BlockTheoryItemProps {
		blockState: BlockRes;
	}

	const { blockState }: BlockTheoryItemProps = $props();
	const block = blockState;

	$effect(() => {
		content = block.type === 'THEORY' ? block.content : '';
	});

	let content: string = $state('');
</script>

<!-- Add main-container class to the root div -->
<div class="rounded-lg bg-white dark:bg-gray-800">
	<TextEditor bind:content />
</div>
