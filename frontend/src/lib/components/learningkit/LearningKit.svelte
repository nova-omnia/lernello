<script lang="ts">
	import { Trash2 } from 'lucide-svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _ } from 'svelte-i18n';
	import { createMutation } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient.js';
	import { deleteLearningKit } from '$lib/api/collections/learningKit.js';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';

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

	async function handleConfirmDelete() {
		if (!uuid) return;

		await $deleteKitMutation.mutateAsync(uuid);

		showDeleteDialog = false;
	}
</script>

<a
	class="text-surface-950-50 card preset-filled-surface-50-950 border-surface-300-700 hover:preset-filled-surface-100-900 relative flex h-36 w-full max-w-52 flex-col items-center justify-center space-y-2 border p-4 text-center"
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
	<p>{title}</p>
</a>

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
