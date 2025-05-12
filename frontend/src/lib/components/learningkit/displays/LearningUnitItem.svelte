<script lang="ts">
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import GenerateLearningUnitModal from '$lib/components/dialogs/GenerateLearningUnitModal.svelte';
	import { _ } from 'svelte-i18n';
	import { AlignLeft, GripVertical, Loader, CheckCircle2 } from 'lucide-svelte';
	import { Progress } from '@skeletonlabs/skeleton-svelte';
	import { dragHandle } from 'svelte-dnd-action';
	import { INSTRUCTOR_ROLE, type RoleType, TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';
	import { createQuery } from '@tanstack/svelte-query';
	import { getLearningUnitProgress } from '$lib/api/collections/progress';
	import { api } from '$lib/api/apiClient';
	import { createDebounced } from '$lib/utils/createDebounced';
	import { toaster } from '$lib/states/toasterState.svelte';
	import type { LearningUnitRes } from '$lib/schemas/response/LearningUnitRes';
	import { renameLearningUnit } from '$lib/api/collections/learningUnit';

	interface LearningUnitProps {
		learningUnit: LearningUnitRes;
		onDeleteLearningUnit: () => void;
		onGenerateLearningUnit: (files: string[]) => void;
		role: RoleType;
		isLoading: boolean;
		invalidateLearningKitQuery: () => void;
	}

	const {
		learningUnit,
		onDeleteLearningUnit,
		onGenerateLearningUnit,
		role,
		isLoading,
		invalidateLearningKitQuery
	}: LearningUnitProps = $props();

	let name = $derived(learningUnit.name);

	const unitProgressQuery = createQuery({
		queryKey: ['learning-unit-progress', learningUnit.uuid],
		queryFn: () => api(fetch).req(getLearningUnitProgress, null, learningUnit.uuid).parse(),
		enabled: role === TRAINEE_ROLE
	});

	let showDeleteDialog = $state(false);
	let showGenerationDialog = $state(false);

	function deleteLearningUnitHandler() {
		onDeleteLearningUnit();
		showDeleteDialog = false;
	}

	let isCompleted = $derived(
		$unitProgressQuery.isSuccess && $unitProgressQuery.data?.progressPercentage === 100
	);

	let progressMeterClass = $derived(isCompleted ? 'bg-green-500' : 'bg-primary-500');
	const onUpdateHandler = createDebounced(async (newName: string) => {
		if ((newName.length < 3 || newName.length > 40) && newName.trim() !== '') {
			toaster.create({
				title: $_('common.warning.title'),
				description: $_('learningUnit.newName.danger'),
				type: 'warning'
			});
			if (newName.trim() !== '') {
				name = learningUnit.name;
			}
			return;
		}
		if (newName.trim() === '') {
			name = learningUnit.name;
			toaster.create({
				title: $_('common.warning.title'),
				description: $_('learningUnit.newName.empty'),
				type: 'warning'
			});
			return;
		}
		if (newName !== learningUnit.name) {
			try {
				const updatedLearningUnit = await api(fetch)
					.req(renameLearningUnit, { name: newName }, learningUnit.uuid)
					.parse();
				learningUnit.name = updatedLearningUnit.name;
				name = updatedLearningUnit.name;
				toaster.create({
					title: $_('common.success.title'),
					description: $_('learningUnit.rename.success'),
					type: 'success'
				});
				invalidateLearningKitQuery();
			} catch (error) {
				console.error('Error:', error);
				toaster.create({
					title: $_('common.error.title'),
					description: $_('learningUnit.rename.error'),
					type: 'error'
				});
				name = learningUnit.name;
			}
		}
	}, 500);

	const handleInputBlur = () => {
		onUpdateHandler(name);
	};

	const handleInputKeydown = (event: KeyboardEvent) => {
		if (event.key === 'Enter') {
			event.preventDefault();
			onUpdateHandler(name);
			(event.target as HTMLElement).blur();
		} else if (event.key === 'Escape') {
			name = learningUnit.name;
			(event.target as HTMLElement).blur();
		}
	};
</script>

<div class="flex items-center">
	{#if role === INSTRUCTOR_ROLE}
		<div use:dragHandle aria-label={`drag-handle for ${learningUnit.name}`}>
			<GripVertical size={28} class="text-surface-300-700" />
		</div>
		<div
			class="card preset-filled-surface-100-900 flex w-full items-center justify-between p-4
		{isLoading ? 'pointer-events-none cursor-not-allowed opacity-50' : ''}"
		>
			<div class="flex w-full max-w-sm items-center gap-4">
				<div class="relative">
					<AlignLeft size={32} />
				</div>
				<label class="label">
					<input
						class="input -m-1 p-1 font-medium"
						type="text"
						placeholder={$_('block.name')}
						bind:value={name}
						onblur={handleInputBlur}
						onkeydown={handleInputKeydown}
					/>
				</label>
			</div>

			{#if role === INSTRUCTOR_ROLE}
				<div class="flex gap-2">
					<a
						type="button"
						class="btn preset-filled-primary-500"
						href={`/learningunit/${learningUnit.uuid}`}
					>
						{$_('common.edit')}
					</a>
					<button
						type="button"
						onclick={(e) => {
							e.preventDefault();
							showGenerationDialog = true;
						}}
						class="btn preset-filled-primary-500"
						disabled={isLoading}
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
						disabled={isLoading}
					>
						{$_('common.delete')}
					</button>
					{#if isLoading}
						<Loader class="ml-4 animate-spin" size={24} />
					{/if}
				</div>
			{/if}
		</div>
	{:else if role === TRAINEE_ROLE}
		<a
			aria-disabled={isLoading}
			class="card preset-filled-surface-100-900 hover:preset-filled-surface-200-800 flex w-full items-center justify-between p-4
		{isLoading ? 'pointer-events-none cursor-not-allowed opacity-50' : ''}
		{isCompleted && role === TRAINEE_ROLE ? 'border-l-4 border-green-500' : ''}"
			href={`/learningunit/${learningUnit.uuid}`}
		>
			<div class="flex w-full max-w-sm items-center gap-4">
				<div class="relative">
					<AlignLeft size={32} />
					{#if role === TRAINEE_ROLE && isCompleted}
						<CheckCircle2
							class="bg-surface-100-900 absolute -right-1 -top-1 h-4 w-4 rounded-full text-green-500"
						/>
					{/if}
				</div>
				<div class="flex w-full flex-col justify-center truncate">
					<h3 class="font-medium">
						{name}
					</h3>
					{#if role === TRAINEE_ROLE && $unitProgressQuery.isSuccess && $unitProgressQuery.data}
						<div class="w-full pt-1">
							<Progress
								value={$unitProgressQuery.data.progressPercentage}
								max={100}
								height="h-1.5"
								meterBg={progressMeterClass}
							/>
						</div>
					{/if}
				</div>
			</div>
		</a>
		{#if isLoading}
			<Loader class="ml-4 animate-spin" size={24} />
		{/if}
	{/if}
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

<GenerateLearningUnitModal
	bind:isOpen={showGenerationDialog}
	isLoading={false}
	onConfirm={(files) => {
		onGenerateLearningUnit(files);
		showGenerationDialog = false;
	}}
	onCancel={() => {
		showGenerationDialog = false;
	}}
/>
