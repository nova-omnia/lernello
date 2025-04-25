<script lang="ts">
	import MultiSelect from '$lib/components/MultiSelect.svelte';
	import { Clock, Settings, UserPlus } from 'lucide-svelte';
	import FileUpload from '$lib/components/FileUpload.svelte';
	import { deleteLearningKit, getLearningKitById } from '$lib/api/collections/learningKit';
	import { goto } from '$app/navigation';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _, locale } from 'svelte-i18n';
	import { updateLearningKit } from '$lib/api/collections/learningKit.js';
	import { api } from '$lib/api/apiClient.js';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { page } from '$app/state';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import type { UpdateLearningKit } from '$lib/schemas/request/UpdateLearningKit';
	import { getAllFiles } from '$lib/api/collections/file';
	import { getAllTrainees } from '$lib/api/collections/user';
	import AddTraineeModal from '$lib/components/dialogs/AddTraineeModal.svelte';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import PageContainer from '$lib/components/PageContainer.svelte';
	import LearningKitTabs from '$lib/components/learningkit/LearningKitTabs.svelte';
	import TraineeDisplay from '$lib/components/learningkit/displays/TraineeDisplay.svelte';
	import FileDisplay from '$lib/components/learningkit/displays/FileDisplay.svelte';

	const learningKitId = page.params.learningKitId;
	const invalidate = useQueryInvalidation();

	const learningKitQuery = createQuery({
		queryKey: ['learning-kit', learningKitId],
		queryFn: () => api(fetch).req(getLearningKitById, null, learningKitId).parse()
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
	const deleteLearningKitMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(deleteLearningKit, null, id).parse(),
		onSuccess: () => {
			invalidate(['latest-learning-kits-list']);
			invalidate(['all-learning-kits-list']);
			goto('/dashboard');
		}
	});

	let showDeleteDialog = $state(false);
	let showAddTraineeModal = $state(false);

	const dateFormat = new Intl.DateTimeFormat($locale || window.navigator.language, {
		year: 'numeric',
		month: '2-digit',
		day: '2-digit',
		hour: '2-digit',
		minute: '2-digit'
	});

	const availableFilesQuery = createQuery({
		queryKey: ['files-list'],
		queryFn: () => api(fetch).req(getAllFiles, null).parse()
	});
	const availableTraineesQuery = createQuery({
		queryKey: ['trainees-list'],
		queryFn: () => api(fetch).req(getAllTrainees, null).parse()
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
			<LearningKitTabs {learningKitId} learningKit={$learningKitQuery.data}></LearningKitTabs>
		</div>

		<!-- trainees -->
		<p class="text-primary-500 mt-5 text-sm font-semibold">{$_('common.trainees')}</p>
		<button
			class="preset-filled-primary-100-900 rounded-border border-surface-200-800 flex w-full items-center justify-center gap-2 rounded-lg border-[1px] p-2 text-center text-base"
			onclick={() => (showAddTraineeModal = true)}
		>
			<UserPlus />
			{$_('learningKit.addNewTrainee')}
		</button>
		<p class="mt-5 text-sm">{$_('trainee.access')}</p>

		<div class="flex flex-col gap-2">
			<MultiSelect
				options={$availableTraineesQuery.data?.map((trainee) => ({
					uuid: trainee.uuid,
					label: `${trainee.username} | ${trainee.name} ${trainee.surname}`
				})) ?? []}
				selected={$learningKitQuery.data.participants.map((trainee) => ({
					uuid: trainee.uuid,
					label: `${trainee.username} | ${trainee.name} ${trainee.surname}`
				}))}
				onSelect={(options) => {
					$updateLearningKitMutation.mutate({
						id: learningKitId,
						data: {
							participants: options.map((opt) => opt.uuid)
						}
					});
				}}
			/>
			{#each $learningKitQuery.data.participants ?? [] as trainee (trainee.uuid)}
				<TraineeDisplay
					user={trainee}
					onRemoveTrainee={() => {
						const current = $learningKitQuery.data.participants;
						const updated = current.filter((t) => t.uuid !== trainee.uuid).map((t) => t.uuid);

						$updateLearningKitMutation.mutate({
							id: learningKitId,
							data: {
								participants: updated
							}
						});
					}}
				/>
			{/each}
		</div>

		<!-- Context -->
		<p class="text-primary-500 mt-5 text-sm font-semibold">{$_('common.context')}</p>
		<FileUpload />
		<p class="mt-5 text-sm">{$_('learningKit.context.description')}</p>
		<div class="flex flex-col gap-2">
			<MultiSelect
				options={$availableFilesQuery.data?.map((file) => ({
					uuid: file.uuid,
					label: `${file.name}`
				})) ?? []}
				selected={$learningKitQuery.data.files?.map((file) => ({
					//gives a warning when not using ?
					uuid: file.uuid,
					label: `${file.name}`
				}))}
				onSelect={(options) => {
					$updateLearningKitMutation.mutate({
						id: learningKitId,
						data: {
							files: options.map((options) => options.uuid)
						}
					});
				}}
			/>

			{#each $learningKitQuery.data.files ?? [] as file (file.uuid)}
				<FileDisplay
					File={file}
					onRemoveFile={() => {
						const current = $learningKitQuery.data.files ?? []; //gives a warning when not using ??
						const updated = current.filter((f) => f.uuid !== file.uuid).map((f) => f.uuid);

						$updateLearningKitMutation.mutate({
							id: learningKitId,
							data: {
								files: updated
							}
						});
					}}
				/>
			{/each}
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

		<AddTraineeModal
			isOpen={showAddTraineeModal}
			onConfirm={async () => {
				showAddTraineeModal = false;

				await invalidate(['trainees-list']);
				const updatedTrainees = await api(fetch).req(getAllTrainees, null).parse();
				const last = updatedTrainees.at(-1);

				if (last) {
					const currentParticipants = $learningKitQuery.data.participants.map((t) => t.uuid);
					await $updateLearningKitMutation.mutate({
						id: learningKitId,
						data: {
							participants: [...currentParticipants, last.uuid]
						}
					});
				}
			}}
			onCancel={() => (showAddTraineeModal = false)}
		/>
	</PageContainer>
{/if}
