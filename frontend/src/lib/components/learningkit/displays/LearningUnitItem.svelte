<script lang="ts">
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _ } from 'svelte-i18n';
	import { dragHandle } from 'svelte-dnd-action';
	import { AlignLeft, GripVertical } from 'lucide-svelte';

	interface LearningUnitProps {
		learningUnit: {
			name: string;
			description: string;
			uuid: string;
		};
		onDeleteLearningUnit: () => void;
	}

	const { learningUnit, onDeleteLearningUnit }: LearningUnitProps = $props();
	let showDeleteDialog = $state(false);

	function deleteLearningUnitHandler() {
		onDeleteLearningUnit();
		showDeleteDialog = false;
	}
</script>

<div class="flex items-center">
	<div use:dragHandle aria-label={`drag-handle for ${learningUnit.name}`}>
		<GripVertical size={28} class="text-surface-300-700" />
	</div>
	<a
		class="card preset-filled-surface-100-900 hover:preset-filled-surface-200-800 flex w-full items-center justify-between p-4"
		href={`/learningunit/${learningUnit.uuid}`}
	>
		<div class="flex w-full max-w-sm items-center gap-4">
			<AlignLeft size={32} />
			<div class="flex w-full flex-col justify-center truncate">
				<p class="truncate font-bold">{learningUnit.name}</p>
				{#if learningUnit.description}
					<p class="truncate">{learningUnit.description}</p>
				{/if}
			</div>
		</div>

		<div class="flex gap-2">
			<button
				type="button"
				onclick={(e) => {
					e.preventDefault();
					showDeleteDialog = true;
				}}
				class="btn preset-filled-error-500"
			>
				{$_('common.delete')}
			</button>
		</div>
	</a>
</div>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title={$_('blocks.delete_title')}
	message={$_('blocks.delete_message')}
	confirmText={$_('common.delete')}
	cancelText={$_('common.cancel')}
	danger={true}
	onConfirm={deleteLearningUnitHandler}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
