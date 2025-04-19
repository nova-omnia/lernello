<!--AITheoryBlock-->
<script lang="ts">
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import MultiSelect from '$lib/components/MultiSelect.svelte';
	import { WandSparkles } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient.js';
	import { getAllFiles } from '$lib/api/collections/file';

	interface Option {
		uuid: string;
		label: string;
	}

	let selectedFiles: Option[] = [];
	let showModal = false;

	const availableFilesQuery = createQuery({
		queryKey: ['files-list'],
		queryFn: () => api(fetch).req(getAllFiles, null).parse()
	});
</script>

<button onclick={() => (showModal = true)} aria-label="Open Wizard">
	<WandSparkles />
</button>

<Modal
	open={showModal}
	contentBase="card bg-surface-100-900 p-4 space-y-4 shadow-xl max-w-lg w-full"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<h2 class="text-center text-lg font-bold">{$_('dialog.creationWizardTitle')}</h2>

		<div class="space-y-4">
			<input
				type="text"
				placeholder={$_('dialog.enterTopicPlaceholder')}
				class="h-input rounded-input shadow-mini focus-visible:ring-dark focus-visible:ring-offset-background focus-visible:outline-hidden w-full px-4 py-2"
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
		</div>

		<div class="flex justify-end pt-4">
			<button class="btn btn-primary" onclick={() => (showModal = false)}>
				{$_('dialog.saveButton')}
			</button>
		</div>
	{/snippet}
</Modal>
