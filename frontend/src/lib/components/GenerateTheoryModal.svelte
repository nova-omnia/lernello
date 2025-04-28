<!--AITheoryBlock-->
<script lang="ts">
	import MultiSelect from '$lib/components/MultiSelect.svelte';
	import { getAllFiles } from '$lib/api/collections/file';
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient.js';
	import { _ } from 'svelte-i18n';
	import FileItem from '$lib/components/learningkit/displays/FileItem.svelte';

	interface GenerateTheoryModalProps {
		isOpen: boolean;
		blockId: string;
		onConfirm: (blockId: string, topic: string, files: string[]) => void;
	}

	let input = $state<string>('');
	let selectedFiles = $state<{ uuid: string; label: string }[]>([]);

	const availableFilesQuery = createQuery({
		queryKey: ['files-list'],
		queryFn: () => api(fetch).req(getAllFiles, null).parse()
	});

	let { isOpen = $bindable(), blockId, onConfirm }: GenerateTheoryModalProps = $props();

	const onCancel = () => {
		isOpen = false;
		input = '';
		selectedFiles = [];
	};
</script>

<Modal
	open={isOpen}
	contentBase="card bg-surface-100-900 p-4 space-y-4 shadow-xl max-w-lg w-full"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<h2 class="text-center text-lg font-bold">{$_('dialog.creationWizardTitle')}</h2>

		<div class="space-y-4">
			<input
				type="text"
				bind:value={input}
				class="input input-bordered w-full"
				placeholder={$_('dialog.enterTopicPlaceholder')}
			/>

			<MultiSelect
				options={$availableFilesQuery.data?.map((file) => ({
					uuid: file.uuid,
					label: file.name
				})) ?? []}
				selected={selectedFiles}
				onSelect={(vals) => (selectedFiles = vals)}
				placeholder={$_('dialog.selectFilesPlaceholder')}
			/>
			{#if selectedFiles.length > 0}
				<div class="mt-1">
					<div class="flex max-h-55 flex-col gap-0.5 overflow-y-auto">
						{#each selectedFiles as file (file.uuid)}
							<FileItem
								File={{ uuid: file.uuid, name: file.label }}
								onRemoveFile={() => {
									selectedFiles = selectedFiles.filter((f) => f.uuid !== file.uuid);
								}}
							/>
						{/each}
					</div>
				</div>
			{/if}
		</div>

		<div class="flex justify-end space-x-2">
			<button class="btn btn-primary" onclick={onCancel}>
				{$_('common.cancel')}
			</button>
			<button
				class="btn btn-primary"
				onclick={() =>
					onConfirm(
						blockId,
						input,
						selectedFiles.map((f) => f.uuid)
					)}
			>
				{$_('common.generate')}
			</button>
		</div>
	{/snippet}
</Modal>
