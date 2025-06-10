<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { createMutation } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import {
		deleteLearningUnit,
		generateLearningUnit,
		getActiveJobId,
		getGenerationStatus
	} from '$lib/api/collections/learningUnit';
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

	let learningUnitsSnapshot = $derived(learningUnits);
	let currentlyDraggingId: string | null = null;
	let loadingMap: Record<string, boolean> = $state({});

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
			toaster.create({
				description: $_('learningUnit.reorder.successDescription'),
				type: 'success'
			});
		},
		onError: (error) => {
			console.error('Error reordering learning units:', error);
			toaster.create({
				description: $_('learningUnit.reorder.errorDescription'),
				type: 'error'
			});
		}
	});

	const deleteLearningUnitMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(deleteLearningUnit, null, id).parse(),
		onSuccess: () => {
			invalidate(['learning-kit', learningKitId]);
			toaster.create({
				description: $_('learningUnit.delete.successDescription'),
				type: 'success'
			});
		},
		onError: (error) => {
			console.error('Error deleting learning unit:', error);
			toaster.create({
				description: $_('learningUnit.delete.errorDescription'),
				type: 'error'
			});
		}
	});

	const generateLearningUnitMutation = createMutation({
		mutationFn: async ({
			id,
			files,
			prompt,
			difficulty,
			options
		}: {
			id: string;
			files: string[];
			prompt: string;
			difficulty: string;
			options: { theory: boolean; questions: boolean; multipleChoice: boolean };
		}) => {
			loadingMap[id] = true;

			try {
				const { jobId } = await api(fetch)
					.req(
						generateLearningUnit,
						{
							fileIds: files,
							prompt,
							difficulty,
							includeTheory: options.theory,
							includeQuestions: options.questions,
							includeMultipleChoice: options.multipleChoice
						},
						id
					)
					.parse();

				await pollUntilJobDone(jobId);
			} finally {
				loadingMap[id] = false;
			}
		},
		onSuccess: () => {
			invalidate(['learning-kit', learningKitId]);
			toaster.create({
				description: $_('learningUnit.generate.generatedDescription'),
				type: 'success'
			});
		},
		onError: (error, variables, context) => {
			console.error('Error generating learning unit:', error, variables, context);
			toaster.create({
				description: $_('learningUnit.generate.errorDescription'),
				type: 'error'
			});
		}
	});

	async function pollUntilJobDone(jobId: string): Promise<void> {
		let attempts = 0;
		const maxAttempts = 5000;
		const delay = 500;

		while (attempts < maxAttempts) {
			const { status } = await api(fetch).req(getGenerationStatus, null, jobId).parse();
			if (status === 'DONE') return;
			if (status === 'FAILED') throw new Error('AI-Generation failed');
			await new Promise((res) => setTimeout(res, delay));
			attempts++;
		}
		throw new Error('AI-Generation timed out');
	}

	$effect(() => {
		for (const unit of learningUnits) {
			api(fetch)
				.req(getActiveJobId, null, unit.uuid)
				.parse()
				.then((res) => {
					if (res?.jobId) {
						loadingMap[unit.uuid] = true;

						pollUntilJobDone(res.jobId).finally(() => {
							loadingMap[unit.uuid] = false;
							invalidate(['learning-kit', learningKitId]);
						});
					}
				})
				.catch(() => {});
		}
	});
</script>

<div class="flex w-full flex-col gap-4 p-4">
	<div class="flex w-full justify-between gap-4">
		<div>
			<h2 class="preset-typo-subtitle">{$_('learningKit.learningUnits')}</h2>
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
					isLoading={loadingMap[learningUnit.uuid] ?? false}
					{learningUnit}
					onDeleteLearningUnit={() => {
						$deleteLearningUnitMutation.mutate(learningUnit.uuid);
					}}
					onGenerateLearningUnit={(files, prompt, difficulty, options) => {
						$generateLearningUnitMutation.mutate({
							id: learningUnit.uuid,
							files,
							prompt,
							difficulty,
							options
						});
					}}
					{role}
					invalidateLearningKitQuery={() => invalidate(['learning-kit', learningKitId])}
				/>
			</div>
		{/each}
	</div>
</div>
