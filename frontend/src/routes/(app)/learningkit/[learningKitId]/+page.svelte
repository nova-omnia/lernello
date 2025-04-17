<script lang="ts">
	import MultiSelect from '$lib/components/MultiSelect.svelte';
	import { Clock, Settings, Plus } from 'lucide-svelte';
	import LearningUnitDisplay from '$lib/components/displays/LearningUnitDisplay.svelte';
	import TraineeDisplay from '$lib/components/displays/TraineeDisplay.svelte';
	import FileUpload from '$lib/components/FileUpload.svelte';
	import FileDisplay from '$lib/components/displays/FileDisplay.svelte';
	import { deleteLearningKit, getLearningKitById } from '$lib/api/collections/learningKit';
	import { goto } from '$app/navigation';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _, locale } from 'svelte-i18n';
	import { updateLearningKit } from '$lib/api/collections/learningKit.js';
	import { deleteLearningUnit } from '$lib/api/collections/learningUnit.js';
	import { api } from '$lib/api/apiClient.js';
	import { createMutation, createQuery, useQueryClient } from '@tanstack/svelte-query';
	import { page } from '$app/state';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import type { UpdateLearningKit } from '$lib/schemas/request/UpdateLearningKit';
	import { getAllFiles } from '$lib/api/collections/file';
	import { getAllTrainees } from '$lib/api/collections/user';
	import AddTraineeModal from '$lib/components/dialogs/AddTraineeModal.svelte';

	const client = useQueryClient();
	const learningKitId = page.params.learningKitId;

	const learningKitQuery = createQuery({
		queryKey: ['learning-kit', learningKitId],
		queryFn: () => api(fetch).req(getLearningKitById, null, learningKitId).parse()
	});
	const updateLearningKitMutation = createMutation({
		mutationFn: ({ id, data }: { id: string; data: UpdateLearningKit }) =>
			api(fetch).req(updateLearningKit, data, id).parse(),
		onSuccess: () => {
			client.invalidateQueries({
				queryKey: ['learning-kit', learningKitId]
			});
		}
	});
	const deleteLearningKitMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(deleteLearningKit, null, id).parse(),
		onSuccess: () => {
			client.invalidateQueries({
				queryKey: ['learning-kit', learningKitId]
			});
			goto('/dashboard');
		}
	});
	const deleteLearningUnitMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(deleteLearningUnit, null, id).parse(),
		onSuccess: () => {
			client.invalidateQueries({
				queryKey: ['learning-kit', learningKitId]
			});
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

{#if $learningKitQuery.status === 'pending'}
	<div class="space-y-4 p-5">
		<p class="placeholder h-8 max-w-64"></p>
		<p class="placeholder max-w-48"></p>
		<p class="placeholder"></p>
		<p class="placeholder"></p>
		<p class="placeholder"></p>
		<p class="placeholder"></p>
	</div>
{:else if $learningKitQuery.status === 'error'}
	<ErrorIllustration>{$_('learningKit.error.loadSingle')}</ErrorIllustration>
{:else}
	<div class="p-5">
		<!--header-->
		<div class="space-between flex items-start justify-between p-1">
			<div>
				<h1 class="h1">{$_('learningKit.title')}: {$learningKitQuery.data.name}</h1>
				{#if $learningKitQuery.data.description}
					<h2 class="preset-typo-subtitle">{$learningKitQuery.data.description}</h2>
				{/if}
				{#if $learningKitQuery.data.deadlineDate}
					<p class="mt-2 flex items-center">
						<Clock class="mr-2 inline-block" />
						{dateFormat.format(new Date($learningKitQuery.data.deadlineDate))}
					</p>
				{/if}
			</div>

			<button type="button" class="btn preset-outlined-surface-500 rounded-full">
				<Settings />
				{$_('button.edit')}
			</button>
		</div>

		<!--content-->
		<p class="text-primary-500 mt-5 text-sm font-semibold">{$_('content')}</p>
		<p class="mt-5 text-sm">{$_('learningKit.content')}</p>
		<div class="grid gap-2">
			{#each $learningKitQuery.data.learningUnits ?? [] as learningUnit (learningUnit.uuid)}
				<LearningUnitDisplay
					{learningUnit}
					onDeleteLearningUnit={() => {
						$deleteLearningUnitMutation.mutate(learningUnit.uuid);
					}}
				/>
			{/each}
		</div>
		<a
			class="btn preset-outlined-surface-500 w-full"
			href={`/learningunit/create-form?learningKitId=${$learningKitQuery.data.uuid}`}
		>
			<Plus />
			{$_('learningUnit.create')}
		</a>

		<!-- trainees -->
		<p class="text-primary-500 mt-5 text-sm font-semibold">{$_('trainee.title')}</p>
		<button
			class="preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center justify-center rounded-lg border-[1px] p-2 text-center text-base"
			onclick={() => (showAddTraineeModal = true)}
		>
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
						const updated = current
							.filter((trainee) => trainee.uuid !== trainee.uuid)
							.map((trainee) => trainee.uuid);

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
		<p class="text-primary-500 mt-5 text-sm font-semibold">{$_('learningKit.context')}</p>
		<p class="mt-5 text-sm">{$_('learningKit.context.description')}</p>
		<div class="flex flex-col gap-2">
			<MultiSelect
				options={$availableFilesQuery.data?.map((file) => ({
					uuid: file.uuid,
					label: `${file.name}`
				})) ?? []}
				selected={$learningKitQuery.data.files?.map((file) => ({ //gives a warning when not using ? 
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
			<FileUpload/>
			{#each $learningKitQuery.data.files ?? [] as file (file.uuid)}
				<FileDisplay
					File={file}
					onRemoveFile={() => {
						alert('remove file not implemented');
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

			await client.invalidateQueries({ queryKey: ['trainees-list'] });
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
{/if}
