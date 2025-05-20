<script lang="ts">
	import { Trash2, CheckCircle2 } from 'lucide-svelte'; // Added CheckCircle2
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _ } from 'svelte-i18n';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient.js';
	import { deleteLearningKit } from '$lib/api/collections/learningKit.js';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import { ProgressRing, Progress } from '@skeletonlabs/skeleton-svelte';
	import { INSTRUCTOR_ROLE, TRAINEE_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';
	import { getLearningKitProgress } from '$lib/api/collections/progress';
	import PublishedStatusIndicator from '$lib/components/learningkit/displays/PublishedStatusIndicator.svelte';
	import { toaster } from '$lib/states/toasterState.svelte';

	const invalidate = useQueryInvalidation();

	interface LearningKitProps {
		title: string;
		uuid: string;
		role: RoleType;
		published: boolean;
		deadlineDate?: string | null;
	}

	const { title, uuid, role, published, deadlineDate }: LearningKitProps = $props();
	let showDeleteDialog = $state(false);
	let isExpired = $derived(deadlineDate && new Date(deadlineDate) < new Date());

	const deleteKitMutation = createMutation({
		onSuccess: () => {
			invalidate(['latest-learning-kits-list']);
			invalidate(['all-learning-kits-list']);
			toaster.create({
				description: $_('learningKit.delete.success.description'),
				type: 'success'
			});
		},
		mutationFn: (kitId: string) => api(fetch).req(deleteLearningKit, null, kitId).parse()
	});

	const kitProgressQuery = createQuery({
		queryKey: ['learning-kit-progress', uuid],
		queryFn: () => api(fetch).req(getLearningKitProgress, null, uuid).parse(),
		enabled: role === TRAINEE_ROLE
	});

	function handleConfirmDelete() {
		if (!uuid) return;
		$deleteKitMutation.mutate(uuid);
		showDeleteDialog = false;
	}

	let isCompleted = $derived(
		$kitProgressQuery.isSuccess && $kitProgressQuery.data?.progressPercentage === 100
	);

	const meterBg = 'bg-green-500';

	let cardCompletedClass = $derived(() => {
		if (isCompleted) {
			return 'border-green-500 preset-filled-success-50-950';
		}
		return 'border-surface-300-700 preset-filled-surface-50-950';
	});
</script>

{#if $deleteKitMutation.isPending}
	<div
		class="text-surface-950-50 card preset-filled-surface-50-950 border-surface-300-700 hover:preset-filled-surface-100-900 relative flex h-36 w-full max-w-52 flex-col items-center justify-center space-y-2 border p-4 text-center"
	>
		<ProgressRing
			value={null}
			size="size-8"
			meterStroke="stroke-primary-500"
			trackStroke="stroke-primary-50-950"
		/>
	</div>
{:else if $deleteKitMutation.isSuccess}
	<div></div>
{:else}
	<a
		class="text-surface-950-50 card hover:preset-filled-surface-100-900 relative flex h-36 w-full max-w-52 flex-col items-center justify-center space-y-2 overflow-ellipsis border p-4 text-center {cardCompletedClass}"
		href="/learningkit/{uuid}?tab=learningUnits"
	>
		{#if role === INSTRUCTOR_ROLE}
			<div class="absolute left-2 top-2">
				<PublishedStatusIndicator {published} />
			</div>
			<button
				class="absolute right-0 top-0 z-10 flex gap-2 p-2"
				onclick={(e) => {
					e.preventDefault();
					showDeleteDialog = true;
				}}
			>
				<Trash2 class="h-4 w-4 text-red-500" />
			</button>
		{/if}
		{#if role === TRAINEE_ROLE && $kitProgressQuery.isSuccess && $kitProgressQuery.data && !isExpired}
			{#if isCompleted}
				<div class="absolute left-2 top-2">
					<CheckCircle2 class="h-6 w-6 text-green-500" />
				</div>
			{/if}
			<div class="absolute bottom-2 left-0 w-full px-4">
				<Progress
					value={$kitProgressQuery.data.progressPercentage}
					max={100}
					height="h-2"
					{meterBg}
				/>
			</div>
		{/if}
		<p class="w-48 truncate">{title}</p>
		{#if role === TRAINEE_ROLE && isExpired}
			<p class="mt-2 text-xs text-red-500">
				{$_('learningKit.expired.short')}
			</p>
		{/if}
	</a>
{/if}

<ConfirmDialog
	isOpen={showDeleteDialog}
	title={$_('dialog.delete.title')}
	message={$_('dialog.delete.message', { values: { name: title } })}
	confirmText={$_('common.delete')}
	danger={true}
	onConfirm={handleConfirmDelete}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
