<script lang="ts">
	import AddAITheoryBlockModal from './../AddAITheoryBlockModal.svelte';
	import TextEditor from '$lib/components/MarkdownEditor/TextEditor.svelte';
	import { WandSparkles } from 'lucide-svelte';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte'; // Add this import

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
	let showAddTraineeModal = $state(false);
</script>

<div>
	<TextEditor content={block.content} onUpdate={handleContentUpdate} />

	<button class="btn btn-primary btn-sm mt-2" onclick={() => (showAddTraineeModal = true)}>
		<WandSparkles />
	</button>
</div>

<AddAITheoryBlockModal bind:isOpen={showAddTraineeModal} blockId={block.uuid} />
