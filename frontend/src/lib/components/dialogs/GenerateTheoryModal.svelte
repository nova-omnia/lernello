<!--AITheoryBlock-->
<script lang="ts">
	import MultiSelect from '$lib/components/select/MultiSelect.svelte';
	import { getAllFiles } from '$lib/api/collections/file';
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient.js';
	import { _ } from 'svelte-i18n';
	import FileItem from '$lib/components/learningkit/displays/FileItem.svelte';

	interface GenerateTheoryModalProps {
		isOpen: boolean;
		isLoading: boolean;
		onConfirm: (topic: string, files: string[]) => void;
	}

	let input = $state<string>('');
	let selectedFiles = $state<{ uuid: string; label: string }[]>([]);

	const availableFilesQuery = createQuery({
		queryKey: ['files-list'],
		queryFn: () => api(fetch).req(getAllFiles, null).parse()
	});

	let { isLoading, isOpen = $bindable(), onConfirm }: GenerateTheoryModalProps = $props();

	const onCancel = () => {
		isOpen = false;
		input = '';
		selectedFiles = [];
	};
</script>

<Modal
	open={isOpen}
	contentBase="card bg-surface-100-900 w-full p-8 space-y-4 shadow-xl max-w-xl border-surface-500"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<h1 class="preset-typo-headline">{$_('dialog.creationWizardTitle')}</h1>

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
								file={{ uuid: file.uuid, name: file.label }}
								onRemoveFile={() => {
									selectedFiles = selectedFiles.filter((f) => f.uuid !== file.uuid);
								}}
							/>
						{/each}
					</div>
				</div>
			{/if}
		</div>

		<footer class="flex justify-end gap-3 pt-2">
			<button
				disabled={isLoading}
				type="button"
				class="btn preset-tonal-surface"
				onclick={onCancel}
			>
				{$_('common.cancel')}
			</button>
			<button
				disabled={isLoading}
				type="button"
				class="btn preset-filled-primary-500"
				onclick={() =>
					onConfirm(
						input,
						selectedFiles.map((f) => f.uuid)
					)}
			>
				{$_('common.generate')}
			</button>
		</footer>
	{/snippet}
</Modal>
