<script lang="ts">
	import AddAITheoryBlockModal from '$lib/components/AddAITheoryBlockModal.svelte';
	import TextEditor from '$lib/components/MarkdownEditor/TextEditor.svelte';
	import { WandSparkles } from 'lucide-svelte';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import { THEORY_BLOCK_TYPE } from '$lib/schemas/response/BlockRes';
	import { INSTRUCTOR_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';

	interface BlockTheoryItemProps {
		block: Extract<BlockRes, { type: typeof THEORY_BLOCK_TYPE }>;
		role: RoleType;
	}

	const { block, role }: BlockTheoryItemProps = $props();
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

<TextEditor content={block.content} onUpdate={handleContentUpdate} {role} />

	{#if role === INSTRUCTOR_ROLE}
		<button class="btn btn-primary btn-sm mt-2" onclick={() => (showAddTraineeModal = true)}>
			<WandSparkles />
		</button>
	{/if}
</div>

<AddAITheoryBlockModal bind:isOpen={showAddTraineeModal} blockId={block.uuid} />
