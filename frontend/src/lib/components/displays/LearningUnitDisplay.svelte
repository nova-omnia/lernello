<script lang="ts">
	import { browserApiClient } from '$lib/api/browserApiClient.js';
	import { deleteLearningUnit, regenerateLearningUnit } from '$lib/api/collections/learningUnit';
	import { invalidate } from '$app/navigation';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _ } from 'svelte-i18n';
	import KitContentItem from './KitContentItem.svelte';
	import { Sparkles } from 'lucide-svelte';

	let showDeleteDialog = $state(false);

	interface LearningUnitProps {
		learningUnit: {
			name: string;
			// description: string;
			uuid: string;
		};
		onDeleteLearningUnit: () => void;
	}
	const { learningUnit, onDeleteLearningUnit }: LearningUnitProps = $props();

	async function regenerateLearningUnitHandler() {
		await browserApiClient.req(regenerateLearningUnit, null, learningUnit.uuid);
	}

	async function deleteLearningUnitHandler() {
		showDeleteDialog = false;
	}
</script>

<KitContentItem name={learningUnit.name}>
	{#snippet actions()}
		<a
			type="button"
			href={`/learningunit/${learningUnit.uuid}`}
			class="btn preset-filled-primary-500 rounded-full"
		>
			{$_('button.open')}
		</a>
		<button
			type="button"
			onclick={regenerateLearningUnitHandler}
			class="btn preset-outlined-surface-500 bg-gray rounded-full"
		>
			<Sparkles />
			{$_('button.regenerate')}
		</button>
		<button
			type="button"
			onclick={(e) => {
				e.preventDefault();
				showDeleteDialog = true;
			}}
			class="btn preset-filled-error-500 rounded-full"
		>
			{$_('button.delete')}
		</button>
	{/snippet}
</KitContentItem>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title="Confirm Deletion"
	message={`Are you sure you want to delete "${learningUnit?.name}"?`}
	confirmText="Delete"
	danger={true}
	onConfirm={() => {
		deleteLearningUnitHandler();
		onDeleteLearningUnit();
	}}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
