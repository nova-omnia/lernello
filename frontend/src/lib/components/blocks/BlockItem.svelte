<script lang="ts">
	import TheoryBlockComponent from './BlockTheoryItem.svelte';
	import MultipleChoiceBlockComponent from './BlockMultipleChoiceItem.svelte';
	import QuestionBlockComponent from './BlockQuestionItem.svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import BlockIconHeader from './BlockIconHeader.svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { X } from 'lucide-svelte';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import { _ } from 'svelte-i18n';

	interface BlockItemProps {
		block: BlockRes;
		learningUnitId: string;
	}

	const { block, learningUnitId }: BlockItemProps = $props();

	const componentMap = {
		THEORY: TheoryBlockComponent,
		QUESTION: QuestionBlockComponent,
		MULTIPLE_CHOICE: MultipleChoiceBlockComponent
	};

	let Component = componentMap[block.type];

	let isConfirmDialogOpen: boolean = $state(false);

	function removeBlock() {
		queueBlockAction({
			type: 'REMOVE_BLOCK',
			blockId: block.uuid
		});
		isConfirmDialogOpen = false;
	}
</script>

<div
	class="group card bg-surface-100 dark:bg-surface-900 border-surface-200 dark:border-surface-800 relative space-y-5 border p-4 shadow transition-all duration-200 hover:shadow-lg"
>
	<BlockIconHeader {block} {learningUnitId} />
	<Component {block} />

	<button
		type="button"
		class="btn preset-filled-error-500 absolute -top-2 -right-2 size-8 rounded-full p-0 opacity-0 transition-opacity duration-200 group-hover:opacity-100"
		onclick={() => (isConfirmDialogOpen = true)}
	>
		<X class="h-4 w-4" />
	</button>

	<ConfirmDialog
		isOpen={isConfirmDialogOpen}
		title={$_('blocks.delete_title')}
		message={$_('blocks.delete_message')}
		confirmText={$_('common.delete')}
		cancelText={$_('common.cancel')}
		danger={true}
		onConfirm={removeBlock}
		onCancel={() => {
			isConfirmDialogOpen = false;
		}}
	/>
</div>
