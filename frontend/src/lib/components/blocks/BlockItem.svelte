<script lang="ts">
	import TheoryBlockComponent from './BlockTheoryItem.svelte';
	import MultipleChoiceBlockComponent from './BlockMultipleChoiceItem.svelte';
	import QuestionBlockComponent from './BlockQuestionItem.svelte';
	import {
		type BlockRes,
		MULTIPLE_CHOICE_BLOCK_TYPE,
		QUESTION_BLOCK_TYPE,
		THEORY_BLOCK_TYPE
	} from '$lib/schemas/response/BlockRes';
	import BlockIconHeader from './BlockIconHeader.svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { Loader2, X } from 'lucide-svelte';
	import {
		blockActionState,
		isBlockSaving,
		queueBlockAction
	} from '$lib/states/blockActionState.svelte';
	import { _ } from 'svelte-i18n';
	import type { RoleType } from '$lib/schemas/response/UserInfo';

	interface BlockItemProps {
		block: BlockRes;
		role: RoleType;
		language: string;
	}

	const { block, role, language }: BlockItemProps = $props();

	let isConfirmDialogOpen: boolean = $state(false);
	let isGenerationLoading: boolean = $state(false);
	let isApplyActionLoading: boolean = $state(false);

	function removeBlock() {
		queueBlockAction({
			type: 'REMOVE_BLOCK',
			blockId: block.uuid
		});
		isConfirmDialogOpen = false;
	}

	$effect(() => {
		const blockSaving = $isBlockSaving;

		if (isGenerationLoading) {
			isApplyActionLoading = true;
		} else if (!blockSaving) {
			isApplyActionLoading = false;
		}
	});
</script>

<div
	class="card bg-surface-100 dark:bg-surface-900 border-surface-200 dark:border-surface-800 group relative space-y-5 border p-4 shadow transition-all duration-200 hover:shadow-lg"
	class:opacity-50={isApplyActionLoading}
	class:pointer-events-none={isApplyActionLoading}
>
	{#if isApplyActionLoading}
		<div
			class="text-surface-400 absolute inset-0 z-10 flex items-center justify-center gap-2 text-sm"
		>
			<Loader2 class="h-40 w-40 animate-spin" />
		</div>
	{/if}
	<div
		class="card bg-surface-100 dark:bg-surface-900 border-surface-200 dark:border-surface-800 group relative space-y-5 border p-4 shadow transition-all duration-200 hover:shadow-lg"
	>
		<BlockIconHeader
			{block}
			{role}
			{language}
			isGenerationLoading={(isLoading) => (isGenerationLoading = isLoading)}
		/>

		{#if block.type === THEORY_BLOCK_TYPE}
			<TheoryBlockComponent {block} {role} {language} />
		{:else if block.type === QUESTION_BLOCK_TYPE}
			<QuestionBlockComponent {block} {role} {language} />
		{:else if block.type === MULTIPLE_CHOICE_BLOCK_TYPE}
			<MultipleChoiceBlockComponent {block} {role} {language} />
		{/if}

		{#if role === 'INSTRUCTOR'}
			<button
				type="button"
				class="btn preset-filled-error-500 absolute -right-2 -top-2 size-8 rounded-full p-0 opacity-0 transition-opacity duration-200 group-hover:opacity-100"
				onclick={() => (isConfirmDialogOpen = true)}
			>
				<X class="h-4 w-4" />
			</button>
		{/if}
	</div>
</div>

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
