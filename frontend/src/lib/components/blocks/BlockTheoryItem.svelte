<script lang="ts">
	import AddAITheoryBlockModal from './../AddAITheoryBlockModal.svelte';
	import TextEditor from '$lib/components/MarkdownEditor/TextEditor.svelte';
	import { WandSparkles } from 'lucide-svelte';

	const { block } = $props();

	let content = block.content;
	let showAddTraineeModal = $state(false);
</script>

<div>
	<TextEditor {content} />

	<button class="btn btn-primary btn-sm mt-2" onclick={() => (showAddTraineeModal = true)}>
		<WandSparkles />
	</button>
</div>

<AddAITheoryBlockModal bind:isOpen={showAddTraineeModal} blockId={block.uuid} />
