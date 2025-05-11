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

	interface LearningUnitProps {
		learningUnit: {
			name: string;
			description?: string;
			uuid: string;
		};
		onDeleteLearningUnit: () => void;
		onGenerateLearningUnit: (files: string[]) => void;
		role: RoleType;
		isLoading: boolean;
	}

	const {
		learningUnit,
		onDeleteLearningUnit,
		onGenerateLearningUnit,
		role,
		isLoading
	}: LearningUnitProps = $props();

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
</script>

<div class="flex items-center">
	<div use:dragHandle aria-label={`drag-handle for ${learningUnit.name}`}>
		{#if role === INSTRUCTOR_ROLE}
			<GripVertical size={28} class="text-surface-300-700" />
		{/if}
	</div>

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
				<p class="truncate font-bold">{learningUnit.name}</p>
				{#if learningUnit.description}
					<p class="truncate">{learningUnit.description}</p>
				{/if}
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

		{#if role === INSTRUCTOR_ROLE}
			<div class="flex gap-2">
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
			</div>
		{/if}
	</a>
	{#if isLoading}
		<Loader class="ml-4 animate-spin" size={24} />
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
