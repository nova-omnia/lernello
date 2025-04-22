<script lang="ts">
	import TextEditor from '$lib/components/MarkdownEditor/TextEditor.svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';

	interface BlockTheoryItemProps {
		blockState: BlockRes;
	}

	const { blockState }: BlockTheoryItemProps = $props();

	$effect(() => {
		content = blockState.type === 'THEORY' ? blockState.content : '';
	});

	let content: string = $state('');
</script>

<!-- Add main-container class to the root div -->
<div class="rounded-lg bg-white dark:bg-gray-800">
	<TextEditor bind:content />
</div>
