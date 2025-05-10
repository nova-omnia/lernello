<script lang="ts">
	import { Clock, Pencil, Trash } from 'lucide-svelte';
	import {
		deleteLearningKit,
		getLearningKitById,
		publishLearningKit,
		updateLearningKit
	} from '$lib/api/collections/learningKit';
	import { goto } from '$app/navigation';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _, locale } from 'svelte-i18n';
	import { api } from '$lib/api/apiClient.js';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { page } from '$app/state';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import LearningKitTabs from '$lib/components/learningkit/LearningKitTabs.svelte';
	import { INSTRUCTOR_ROLE, TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';
	import LearningUnitsTab from '$lib/components/learningkit/tab/LearningUnitsTab.svelte';
	import type { UpdateLearningKit } from '$lib/schemas/request/UpdateLearningKit'; // Import the role constant
	import { markLearningKitOpened } from '$lib/api/collections/progress';
	import { toaster } from '$lib/states/toasterState.svelte';

	const learningKitId = page.params.learningKitId;
	const invalidate = useQueryInvalidation();

	const { data } = $props();

	const learningKitQuery = createQuery({
		queryKey: ['learning-kit', learningKitId],
		queryFn: () => api(fetch).req(getLearningKitById, null, learningKitId).parse()
	});
	const deleteLearningKitMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(deleteLearningKit, null, id).parse(),
		onSuccess: () => {
			invalidate(['latest-learning-kits-list']);
			invalidate(['all-learning-kits-list']);
			goto('/dashboard');
		}
	});
	const publishLearningKitMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(publishLearningKit, null, id).parse()
	});
	const updateLearningKitMutation = createMutation({
		mutationFn: ({ id, data }: { id: string; data: UpdateLearningKit }) =>
			api(fetch).req(updateLearningKit, data, id).parse(),
		onSuccess: () => {
			invalidate(['latest-learning-kits-list']);
			invalidate(['all-learning-kits-list']);
			invalidate(['learning-kit', learningKitId]);
		}
	});

	let showDeleteDialog = $state(false);
	let showPublishDialog = $state(false);

	const dateFormat = new Intl.DateTimeFormat($locale || window.navigator.language, {
		year: 'numeric',
		month: '2-digit',
		day: '2-digit',
		hour: '2-digit',
		minute: '2-digit'
	});

	$effect.pre(() => {
		if (
			$learningKitQuery.data &&
			data.userInfo.role === TRAINEE_ROLE &&
			$learningKitQuery.data.uuid
		) {
			api(fetch)
				.req(markLearningKitOpened, { learningKitId: $learningKitQuery.data.uuid })
				.parse()
				.catch((err) => {
					console.error('Failed to mark learning kit as opened:', err);
					toaster.create({
						title: $_('learningKit.error.markOpened'),
						description: $_('error.description', { values: { status: 'unknown' } }),
						type: 'error'
					});
				});
		}
	});
</script>

{#snippet learningKitLoading()}
	<div class="space-y-4 p-5">
		<p class="placeholder h-8 max-w-64"></p>
		<p class="placeholder max-w-48"></p>
		<p class="placeholder"></p>
		<p class="placeholder"></p>
		<p class="placeholder"></p>
		<p class="placeholder"></p>
	</div>
{/snippet}

{#if $learningKitQuery.status === 'pending'}
	{@render learningKitLoading()}
{:else if $learningKitQuery.status === 'error'}
	<ErrorIllustration>{$_('learningKit.error.loadSingle')}</ErrorIllustration>
{:else}
	<PageContainer>
		{#if data.userInfo.role === INSTRUCTOR_ROLE}
			<div class="flex flex-col gap-8">
				<div class="flex-col">
					<div class="flex w-full justify-between gap-4">
						<div>
							<h1 class="preset-typo-headline">
								{$_('learningKit.title', { values: { name: $learningKitQuery.data?.name } })}
							</h1>
							{#if $learningKitQuery.data?.deadlineDate}
								<p class="flex items-center gap-2">
									<Clock size={20} />
									{dateFormat.format(new Date($learningKitQuery.data.deadlineDate))}
								</p>
							{/if}
						</div>
						<div class="flex h-10 gap-8">
							<div class="flex gap-2">
								<button
									type="button"
									class="btn preset-filled-primary-500 h-full"
									onclick={() => {
										showPublishDialog = true;
									}}
									>{$learningKitQuery.data?.published
										? $_('learningKit.unpublish.text')
										: $_('learningKit.publish.text')}
								</button>
								<button
									type="button"
									class="btn preset-outlined-surface-500 h-full"
									onclick={() => goto(`/learningkit/${$learningKitQuery.data.uuid}/edit-form`)}
								>
									<Pencil size={20} />
								</button>
							</div>
							<button
								onclick={() => {
									showDeleteDialog = true;
								}}
								type="button"
								class="btn preset-filled-error-500 h-full"
							>
								<Trash size={20} />
							</button>
						</div>
					</div>
					{#if $learningKitQuery.data?.description}
						<p class="w-2xl wrap-break-word">{$learningKitQuery.data.description}</p>
					{/if}
				</div>
				<LearningKitTabs
					learningKit={$learningKitQuery.data}
					tab={data.tab}
					role={data.userInfo.role}
				></LearningKitTabs>
			</div>
		{:else if data.userInfo.role === TRAINEE_ROLE}
			<LearningUnitsTab
				learningKitId={$learningKitQuery.data.uuid}
				learningUnits={$learningKitQuery.data.learningUnits ?? []}
				role={data.userInfo.role}
			></LearningUnitsTab>
		{/if}
	</PageContainer>

	<ConfirmDialog
		isOpen={showDeleteDialog}
		title="Confirm Deletion"
		message={`Are you sure you want to delete "${$learningKitQuery.data.name}"?`}
		confirmText="Delete"
		danger={true}
		onConfirm={() => {
			$deleteLearningKitMutation.mutate($learningKitQuery.data.uuid);
			showDeleteDialog = false;
		}}
		onCancel={() => {
			showDeleteDialog = false;
		}}
	/>

	<ConfirmDialog
		isOpen={showPublishDialog}
		title={$learningKitQuery.data?.published
			? $_('learningKit.unpublish.confirmationTitle')
			: $_('learningKit.publish.confirmationTitle')}
		message={$learningKitQuery.data?.published
			? $_('learningKit.unpublish.confirmationText', {
					values: { name: $learningKitQuery.data.name }
				})
			: $_('learningKit.publish.confirmationText', {
					values: { name: $learningKitQuery.data.name }
				})}
		confirmText={$learningKitQuery.data?.published
			? $_('learningKit.unpublish.text')
			: $_('learningKit.publish.text')}
		danger={false}
		onConfirm={() => {
			if ($learningKitQuery.data?.published) {
				$updateLearningKitMutation.mutate({
					id: learningKitId,
					data: { published: false }
				});
			} else {
				$updateLearningKitMutation.mutate({
					id: $learningKitQuery.data.uuid,
					data: { published: true }
				});
				$publishLearningKitMutation.mutate(learningKitId);
			}

			showPublishDialog = false;
		}}
		onCancel={() => {
			showPublishDialog = false;
		}}
	/>
{/if}
