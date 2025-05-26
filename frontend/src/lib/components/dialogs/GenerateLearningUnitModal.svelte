<!--LearningUnitModal.svelte-->
<script lang="ts">
	import MultiSelect from '$lib/components/select/MultiSelect.svelte';
	import { getAllFiles } from '$lib/api/collections/file';
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient.js';
	import { _ } from 'svelte-i18n';
	import FileItem from '$lib/components/learningkit/displays/FileItem.svelte';

	interface GenerateLearningUnitModalProps {
		isOpen: boolean;
		isLoading: boolean;
		onConfirm: (
			files: string[],
			prompt: string,
			difficulty: string,
			options: { theory: boolean; questions: boolean; multipleChoice: boolean }
		) => void;
		onCancel: () => void;
	}

	let selectedFiles = $state<{ uuid: string; label: string }[]>([]);
	let personalPrompt = $state('');
	let difficulty = $state('');
	let includeTheory = $state(true);
	let includeQuestions = $state(true);
	let includeMultipleChoice = $state(true);

	const difficultyOptions = [
		{ value: 'easy', label: $_('dialog.difficulty.easy') },
		{ value: 'medium', label: $_('dialog.difficulty.medium') },
		{ value: 'hard', label: $_('dialog.difficulty.hard') }
	];

	const availableFilesQuery = createQuery({
		queryKey: ['files-list'],
		queryFn: () => api(fetch).req(getAllFiles, null).parse()
	});

	let {
		// isLoading,
		onConfirm,
		onCancel,
		isOpen = $bindable()
	}: GenerateLearningUnitModalProps = $props();
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
				bind:value={personalPrompt}
				placeholder={$_('dialog.personalPromptPlaceholder')}
				class="input w-full"
			/>

			<select
				bind:value={difficulty}
				class="input bg-surface-200 text-surface-900 dark:bg-surface-800 dark:text-surface-100 border-surface-300 dark:border-surface-600 w-full rounded border"
			>
				<option value="" disabled selected hidden>
					{$_('dialog.difficulty.placeholder')}
				</option>
				{#each difficultyOptions as option (option.value)}
					<option value={option.value}>{option.label}</option>
				{/each}
			</select>

			<p class="preset-typo-body ml-1 block">
				{$_('dialog.blockType.label')}
			</p>
			<div class="ml-1 flex flex-row items-center gap-6">
				<label class="flex items-center gap-2">
					<input type="checkbox" bind:checked={includeTheory} class="checkbox" />
					<span>{$_('block.theoryBlock')}</span>
				</label>

				<label class="flex items-center gap-2">
					<input type="checkbox" bind:checked={includeQuestions} class="checkbox" />
					<span>{$_('block.questionBlock')}</span>
				</label>

				<label class="flex items-center gap-2">
					<input type="checkbox" bind:checked={includeMultipleChoice} class="checkbox" />
					<span>{$_('block.multipleChoiceQuiz')}</span>
				</label>
			</div>

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
								isForModal={true}
							/>
						{/each}
					</div>
				</div>
			{/if}
		</div>

		<footer class="flex justify-end gap-3 pt-2">
			<button
				type="button"
				class="btn preset-tonal-surface"
				onclick={() => {
					onCancel();
					selectedFiles = [];
				}}
			>
				{$_('common.cancel')}
			</button>
			<button
				disabled={!includeMultipleChoice && !includeQuestions && !includeTheory}
				type="button"
				class="btn preset-filled-primary-500"
				onclick={() =>
					onConfirm(
						selectedFiles.map((f) => f.uuid),
						personalPrompt,
						difficulty,
						{
							theory: includeTheory,
							questions: includeQuestions,
							multipleChoice: includeMultipleChoice
						}
					)}
			>
				{$_('common.generate')}
			</button>
		</footer>
	{/snippet}
</Modal>
