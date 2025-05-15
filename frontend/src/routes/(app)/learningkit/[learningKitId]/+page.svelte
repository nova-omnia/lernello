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
	import PublishedStatusIndicator from '$lib/components/learningkit/displays/PublishedStatusIndicator.svelte';

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
			toaster.create({
				description: $_('learningKit.delete.success.description'),
				type: 'success'
			});
		},
		onError: (error) => {
			console.error('Failed to delete learning kit:', error);
			toaster.create({
				description: $_('learningKit.delete.error.description'),
				type: 'error'
			});
		}
	});

	const publishLearningKitMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(publishLearningKit, null, id).parse(),
		onSuccess: () => {
			invalidate(['latest-learning-kits-list']);
			invalidate(['all-learning-kits-list']);
			invalidate(['learning-kit', learningKitId]);
			toaster.create({
				description: $_('learningKit.publish.success.description'),
				type: 'success'
			});
		},
		onError: (error) => {
			console.error('Failed to publish learning kit:', error);
			toaster.create({
				description: $_('learningKit.publish.error.description'),
				type: 'error'
			});
		}
	});

	const updateLearningKitMutation = createMutation({
		mutationFn: ({ id, data }: { id: string; data: UpdateLearningKit }) =>
			api(fetch).req(updateLearningKit, data, id).parse(),
		onSuccess: () => {
			invalidate(['latest-learning-kits-list']);
			invalidate(['all-learning-kits-list']);
			invalidate(['learning-kit', learningKitId]);
			toaster.create({
				description: $_('learningKit.update.success.description'),
				type: 'success'
			});
		},
		onError: (error) => {
			console.error('Failed to update learning kit:', error);
			toaster.create({
				description: $_('learningKit.update.error.description'),
				type: 'error'
			});
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
							<h1 class="preset-typo-headline flex items-center gap-2">
								{#if $learningKitQuery.data?.published !== undefined}
									<PublishedStatusIndicator
										published={$learningKitQuery.data.published}
										size="h-4 w-4"
									/>
								{/if}
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
							<div class="mr-6 flex gap-2">
								<button
									class="btn preset-outlined-surface-500 h-full"
									onclick={() =>
										goto(`/learningkit/${$learningKitQuery.data.uuid}/edit-form`, {
											replaceState: true
										})}
								>
									<Pencil size={20} />
								</button>
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
							</div>
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
		title={$_('learningKit.delete.confirmationTitle')}
		message={$_('learningKit.delete.confirmationMessage', {
			values: { name: $learningKitQuery.data.name }
		})}
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
			? $_('learningKit.unpublish.confirmationTitle', {
					values: { name: $learningKitQuery.data.name }
				})
			: $_('learningKit.publish.confirmationTitle', {
					values: { name: $learningKitQuery.data.name }
				})}
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
				$publishLearningKitMutation.mutate(learningKitId);
			}

			showPublishDialog = false;
		}}
		onCancel={() => {
			showPublishDialog = false;
		}}
	/>
{/if}
