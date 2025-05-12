<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { createMutation } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { deleteLearningUnit, generateLearningUnit } from '$lib/api/collections/learningUnit';
	import { updateLearningUnitsOrder } from '$lib/api/collections/learningKit';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import { type DndEvent, dragHandleZone, TRIGGERS } from 'svelte-dnd-action';
	import { flip } from 'svelte/animate';
	import LearningUnitItem from '$lib/components/learningkit/displays/LearningUnitItem.svelte';
	import { Plus } from 'lucide-svelte';
	import { INSTRUCTOR_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';
	import { toaster } from '$lib/states/toasterState.svelte';
	import type { LearningUnitRes } from '$lib/schemas/response/LearningUnitRes';
	import { initializeInstructorDnd } from '$lib/states/dndService';

	const invalidate = useQueryInvalidation();

	interface LearningUnitsProps {
		learningKitId: string;
		learningUnits: LearningUnitRes[];
		role: RoleType;
	}

	const { learningKitId, learningUnits, role }: LearningUnitsProps = $props();

	initializeInstructorDnd();

	let learningUnitsSnapshot = $derived(learningUnits.map((unit) => ({ ...unit, uuid: unit.uuid })));
	let currentlyDraggingId: string | null = null;

	function handleSortOnConsider(e: CustomEvent<DndEvent<LearningUnitRes>>) {
		learningUnitsSnapshot = e.detail.items;
		if (e.detail.info.trigger === TRIGGERS.DRAG_STARTED) {
			currentlyDraggingId = e.detail.info.id;
		}
	}

	function handleSortOnFinalize(e: CustomEvent<DndEvent<LearningUnitRes>>) {
		if (!currentlyDraggingId) {
			throw new Error('No currently dragging ID');
		}
		const newIdx = e.detail.items.findIndex((unit) => unit.uuid === currentlyDraggingId);
		const oldIdx = learningUnits.findIndex((unit) => unit.uuid === currentlyDraggingId);

		if (newIdx === oldIdx) return;
		learningUnitsSnapshot = e.detail.items;
		$updateLearningUnitOrderMutation.mutate({
			learningUnitUuidsInOrder: learningUnitsSnapshot.map((unit) => unit.uuid)
		});
	}

	const updateLearningUnitOrderMutation = createMutation({
		mutationFn: (newOrder: { learningUnitUuidsInOrder: string[] }) => {
			return api(fetch).req(updateLearningUnitsOrder, newOrder, learningKitId).parse();
		},
		onSuccess: () => {
			invalidate(['learning-kit', learningKitId]);
		}
	});

	const deleteLearningUnitMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(deleteLearningUnit, null, id).parse(),
		onSuccess: () => {
			invalidate(['learning-kit', learningKitId]);
		}
	});

	const generateLearningUnitMutation = createMutation({
		onMutate: () => {
			toaster.create({
				title: $_('learningUnit.generate.isGenerating'),
				type: 'info'
			});
		},
		mutationFn: ({ id, files }: { id: string; files: string[] }) =>
			api(fetch).req(generateLearningUnit, { fileIds: files }, id).parse(),
		onSuccess: () => {
			invalidate(['learning-kit', learningKitId]);
			toaster.create({
				title: $_('learningUnit.generate.generated'),
				type: 'info'
			});
		}
	});
</script>

<div class="flex w-full flex-col gap-4 p-4">
	<div class="flex w-full justify-between gap-4">
		<div>
			<h2 class="preset-typo-subtitle">{$_('learningKit.learningUnits')}</h2>
			<p>{$_('learningKit.learningUnit.description')}</p>
		</div>
		{#if role === INSTRUCTOR_ROLE}
			<a
				class="btn preset-outlined-surface-500 h-fit"
				href={`/learningunit/create-form?learningKitId=${learningKitId}`}
			>
				<Plus size={24} />
				{$_('learningUnit.create')}
			</a>
		{/if}
	</div>
	<div
		class="grid gap-1"
		use:dragHandleZone={{
			items: learningUnitsSnapshot,
			flipDurationMs: 200,
			dropFromOthersDisabled: true,
			dropTargetStyle: { outline: '1px dashed oklch(45.77% 0.07 211.76deg)', borderRadius: '.5rem' }
		}}
		onconsider={handleSortOnConsider}
		onfinalize={handleSortOnFinalize}
	>
		{#each learningUnitsSnapshot as learningUnit (learningUnit.uuid)}
			<div class="block" animate:flip={{ duration: 200 }}>
				<LearningUnitItem
					isLoading={$generateLearningUnitMutation.isPending}
					{learningUnit}
					onDeleteLearningUnit={() => {
						$deleteLearningUnitMutation.mutate(learningUnit.uuid);
					}}
					onGenerateLearningUnit={(files) => {
						$generateLearningUnitMutation.mutate({
							id: learningUnit.uuid,
							files
						});
					}}
					{role}
					invalidateLearningKitQuery={() => invalidate(['learning-kit', learningKitId])}
				/>
			</div>
		{/each}
	</div>
</div>
