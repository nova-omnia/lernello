<script lang="ts">
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import MultiSelect from './MultiSelect.svelte';
	import { WandSparkles } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';

	interface Option {
		uuid: string;
		label: string;
	}

	let selectedFiles: Option[] = [];
	let showModal = false;

	const files: Option[] = [
		{ uuid: '550e8400-e29b-41d4-a716-446655440000', label: 'Mathematics' },
		{ uuid: '550e8400-e29b-41d4-a716-446655440001', label: 'Artificial Intelligence' },
		{ uuid: '550e8400-e29b-41d4-a716-446655440002', label: 'Machine Learning' },
		{ uuid: '550e8400-e29b-41d4-a716-446655440003', label: 'Computer Vision' },
		{ uuid: '550e8400-e29b-41d4-a716-446655440004', label: 'NLP' }
	];
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
		<h2 class="text-lg font-bold text-center">{$_('dialog.creationWizardTitle')}</h2>

		<div class="space-y-4">
			<input
				type="text"
				placeholder={$_('dialog.enterTopicPlaceholder')}
				class="h-input w-full rounded-input px-4 py-2 shadow-mini focus-visible:ring-dark focus-visible:ring-offset-background focus-visible:outline-hidden"
			/>

			<MultiSelect
				selected={selectedFiles}
				onSelect={(vals) => (selectedFiles = vals)}
				options={files}
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
