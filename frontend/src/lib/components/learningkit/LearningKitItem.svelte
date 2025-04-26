<script lang="ts">
	import { Trash2 } from 'lucide-svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _ } from 'svelte-i18n';
	import { createMutation } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient.js';
	import { deleteLearningKit } from '$lib/api/collections/learningKit.js';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import { ProgressRing } from '@skeletonlabs/skeleton-svelte';

	const invalidate = useQueryInvalidation();

	interface LearningKitProps {
		title: string;
		uuid: string;
	}

	const { title, uuid }: LearningKitProps = $props();
	let showDeleteDialog = $state(false);

	const deleteKitMutation = createMutation({
		onSuccess: () => {
			invalidate(['latest-learning-kits-list']);
			invalidate(['all-learning-kits-list']);
		},
		mutationFn: (kitId: string) => api(fetch).req(deleteLearningKit, null, kitId).parse()
	});

	function handleConfirmDelete() {
		if (!uuid) return;
		$deleteKitMutation.mutate(uuid);
		showDeleteDialog = false;
	}
</script>

{#if $deleteKitMutation.isPending}
	<div
		class="text-surface-950-50 card preset-filled-surface-50-950 border-surface-300-700 hover:preset-filled-surface-100-900 relative flex h-36 w-full max-w-52 flex-col items-center justify-center space-y-2 border p-4 text-center"
	>
		<ProgressRing
			value={null}
			size="size-8"
			meterStroke="stroke-primary-500"
			trackStroke="stroke-primary-50-950"
		/>
	</div>
{:else if $deleteKitMutation.isSuccess}
	<div></div>
{:else}
	<a
		class="text-surface-950-50 card preset-filled-surface-50-950 border-surface-300-700 hover:preset-filled-surface-100-900 relative flex h-36 w-full max-w-52 flex-col items-center justify-center space-y-2 border p-4 text-center overflow-ellipsis"
		href="/learningkit/{uuid}"
	>
		<button
			class="absolute top-0 right-0 z-10 flex gap-2 p-2"
			onclick={(e) => {
				e.preventDefault();
				showDeleteDialog = true;
			}}
		>
			<Trash2 class="h-4 w-4 text-red-500" />
		</button>
		<p class="w-48 truncate">{title}</p>
	</a>
{/if}

<ConfirmDialog
	isOpen={showDeleteDialog}
	title={$_('dialog.delete.title')}
	message={$_('dialog.delete.message', { values: { name: title } })}
	confirmText={$_('common.delete')}
	danger={true}
	onConfirm={handleConfirmDelete}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
