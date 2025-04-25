<script lang="ts">
	import TheoryBlockComponent from './BlockTheoryItem.svelte';
	import MultipleChoiceBlockComponent from './BlockMultipleChoiceItem.svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import BlockIconHeader from './BlockIconHeader.svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { X } from 'lucide-svelte';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import { _ } from 'svelte-i18n';
	import { toaster } from '$lib/states/toasterState.svelte';

	interface BlockItemProps {
		block: BlockRes;
	}

	const { block }: BlockItemProps = $props();

	let blockState = $state(block);

	// TODO: Improve mapping similar to BlockIcon.svelte
	let Component =
		blockState.type === 'THEORY' ? TheoryBlockComponent : MultipleChoiceBlockComponent;

	let isConfirmDialogOpen: boolean = $state(false);

	function debounce(func: () => void, delay: number) {
		let timer: ReturnType<typeof setTimeout>;
		return () => {
			clearTimeout(timer);
			timer = setTimeout(() => {
				func();
			}, delay);
		};
	}

	const debouncedUpdate = debounce(updateBlock, 750);

	$effect(() => {
		debouncedUpdate();
	});

	function removeBlock() {
		queueBlockAction({
			type: 'REMOVE_BLOCK',
			blockId: block.uuid
		});
		isConfirmDialogOpen = false;
	}

	function updateBlock() {
		switch (blockState.type) {
			case 'THEORY':
				queueBlockAction({
					type: 'UPDATE_BLOCK',
					blockId: blockState.uuid,
					data: {
						type: blockState.type,
						name: blockState.name,
						content: blockState.content
					}
				});
				break;

			case 'MULTIPLE_CHOICE':
				queueBlockAction({
					type: 'UPDATE_BLOCK',
					blockId: blockState.uuid,
					data: {
						type: blockState.type,
						name: blockState.name,
						question: blockState.question,
						possibleAnswers: blockState.possibleAnswers,
						correctAnswers: blockState.correctAnswers
					}
				});
				break;

			default:
				console.warn(`Unhandled block type: ${blockState.type}`);
				toaster.create({
					title: $_('error.title'),
					description: $_('error.description', { values: { status: 'unknown' } }),
					type: 'error'
				});
		}
	}
</script>

<div
	class="group card bg-surface-100 dark:bg-surface-900 border-surface-200 dark:border-surface-800 relative space-y-5 border p-4 shadow transition-all duration-200 hover:shadow-lg"
>
	<BlockIconHeader {block} />
	<Component {blockState} />

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
