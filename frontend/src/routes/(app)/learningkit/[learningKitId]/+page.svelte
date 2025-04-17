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
	const deletLearningUnitMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(deleteLearningUnit, null, id).parse(),
		onSuccess: () => {
			client.invalidateQueries({
				queryKey: ['learning-kit', learningKitId]
			});
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

	const availableFilesQuery = createQuery({
		queryKey: ['files-list'],
		queryFn: () => api(fetch).req(getAllFiles, null).parse()
	});
	const availableTrainesQuery = createQuery({
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
						$deletLearningUnitMutation.mutate(learningUnit.uuid);
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
		<p class="mt-5 text-sm">{$_('trainee.access')}</p>

		<div class="flex flex-col gap-2">
			<MultiSelect
				options={[
					{ uuid: '__add__', label: $_('learningKit.addNewTrainee') },
					...($availableTrainesQuery.data?.map((trainee) => ({
						uuid: trainee.uuid,
						label: `${trainee.username} | ${trainee.name} ${trainee.surname}`
					})) ?? [])
				]}
				selected={$learningKitQuery.data.participants.map((trainee) => ({
					uuid: trainee.uuid,
					label: `${trainee.username} | ${trainee.name} ${trainee.surname}`
				}))}
				onSelect={(options) => {
					$updateLearningKitMutation.mutate({
						id: learningKitId,
						data: {
							participants: options.map((options) => options.uuid)
						}
					});
				}}
			/>
			{#each $learningKitQuery.data.participants ?? [] as trainee (trainee.uuid)}
				<TraineeDisplay
					user={trainee}
					onRemoveTrainee={() => {
						alert('remove trainee not implemented');
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
				selected={$updateLearningKitMutation.data?.files?.map((file) => ({
					uuid: file.uuid,
					label: `${file.name}`
				})) ?? []}
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
{/if}
