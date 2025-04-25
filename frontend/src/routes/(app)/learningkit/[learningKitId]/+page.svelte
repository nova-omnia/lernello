<script lang="ts">
	import { Clock, Settings } from 'lucide-svelte';
	import { deleteLearningKit, getLearningKitById } from '$lib/api/collections/learningKit';
	import { goto } from '$app/navigation';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _, locale } from 'svelte-i18n';
	import { api } from '$lib/api/apiClient.js';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { page } from '$app/state';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import PageContainer from '$lib/components/PageContainer.svelte';
	import LearningKitTabs from '$lib/components/learningkit/LearningKitTabs.svelte';

	const learningKitId = page.params.learningKitId;
	const invalidate = useQueryInvalidation();

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
	let showDeleteDialog = $state(false);

	const dateFormat = new Intl.DateTimeFormat($locale || window.navigator.language, {
		year: 'numeric',
		month: '2-digit',
		day: '2-digit',
		hour: '2-digit',
		minute: '2-digit'
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

{#snippet header()}
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
			<button type="button" class="btn preset-outlined-surface-500 h-fit py-4">
				<Settings size={20} />
			</button>
		</div>
		{#if $learningKitQuery.data?.description}
			<p>{$learningKitQuery.data.description}</p>
		{/if}
	</div>
{/snippet}

{#if $learningKitQuery.status === 'pending'}
	{@render learningKitLoading()}
{:else if $learningKitQuery.status === 'error'}
	<ErrorIllustration>{$_('learningKit.error.loadSingle')}</ErrorIllustration>
{:else}
	<PageContainer>
		<div class="flex flex-col gap-8">
			{@render header()}
			<LearningKitTabs learningKit={$learningKitQuery.data}></LearningKitTabs>
		</div>

		<p class="text-primary-500 mt-5 text-sm font-semibold">{$_('learningKit.settings')}</p>
		<p class="mt-5 text-sm">{$_('learningKit.settings.change')}</p>
		<div class="flex gap-2">
			<button type="button" class="btn preset-filled-primary-500 rounded-full"
				>{$_('learningKit.publish')}</button
			>
			<button
				onclick={(e) => {
					e.preventDefault();
					showDeleteDialog = true;
				}}
				type="button"
				class="btn preset-filled-error-500 rounded-full"
				>{$_('learningKit.delete')}
			</button>
		</div>
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
{/if}
