<script lang="ts">
	import AddAITheoryBlockModal from './../AddAITheoryBlockModal.svelte';
	import TextEditor from '$lib/components/MarkdownEditor/TextEditor.svelte';
	import { WandSparkles } from 'lucide-svelte';


	let content: string = '';
	let showAddTraineeModal = $state(false);

</script>
<div>
	<TextEditor {content} />

	<button
		class="btn btn-primary btn-sm mt-2"
		onclick={() => (showAddTraineeModal = true)}
	>
	<WandSparkles/>	
	</button>
</div>

<AddAITheoryBlockModal
	isOpen={showAddTraineeModal}
	onCancel={() => (showAddTraineeModal = false)}
	onConfirm={() => (showAddTraineeModal = false)}
/>
