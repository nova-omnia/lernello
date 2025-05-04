<script lang="ts">
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _ } from 'svelte-i18n';
	import { AlignLeft, GripVertical } from 'lucide-svelte';
	import { INSTRUCTOR_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';

	interface LearningUnitProps {
		learningUnit: {
			name: string;
			description?: string;
			uuid: string;
		};
		onDeleteLearningUnit: () => void;
		role: RoleType;
	}

	const { learningUnit, onDeleteLearningUnit, role }: LearningUnitProps = $props();
	let showDeleteDialog = $state(false);
	let showGenerationDialog = $state(false);

	function deleteLearningUnitHandler() {
		onDeleteLearningUnit();
		showDeleteDialog = false;
	}
</script>

<div class="flex items-center">
	{#if role === INSTRUCTOR_ROLE}
		<GripVertical size={28} class="text-surface-300-700" />
	{/if}
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

		{#if role === INSTRUCTOR_ROLE}
			<div class="flex gap-2">
				<button
					type="button"
					onclick={(e) => {
						e.preventDefault();
						showGenerationDialog = true;
					}}
					class="btn preset-filled-primary-500"
				>
					{$_('common.generate')}
				</button>
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
		{/if}
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
