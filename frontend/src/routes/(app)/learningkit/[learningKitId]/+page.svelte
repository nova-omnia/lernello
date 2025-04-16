<script lang="ts">
	import { Clock, Settings, Plus, UserRoundPlus } from 'lucide-svelte';
	import LearningUnitDisplay from '$lib/components/displays/LearningUnitDisplay.svelte';
	import TraineeDisplay from '$lib/components/displays/TraineeDisplay.svelte';
	import FileUpload from '$lib/components/FileUpload.svelte';
	import TraineeSelectModal from '$lib/components/dialogs/TraineeSelectModal.svelte';
	import FileSelectModal from '$lib/components/dialogs/FileSelectModal.svelte';
	import { Upload } from 'lucide-svelte';
	import FileDisplay from '$lib/components/displays/FileDisplay.svelte';
	import { deleteLearningKit, getLearningKitById } from '$lib/api/collections/learningKit';
	import { goto, invalidate } from '$app/navigation';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _, getLocaleFromNavigator, locale } from 'svelte-i18n';
	import { updateLearningKit } from '$lib/api/collections/learningKit.js';
	import type { ParticipantUser } from '$lib/schemas/response/ParticipantUser';
	import type { FileRes } from '$lib/schemas/response/FileRes';
	import { deleteLearningUnit } from '$lib/api/collections/learningUnit.js';
	import { api } from '$lib/api/apiClient.js';
	import { createMutation, createQuery, useQueryClient } from '@tanstack/svelte-query';
	import { page } from '$app/state';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';
	import type { UpdateLearningKit } from '$lib/schemas/request/UpdateLearningKit';

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

	let selectedFiles = $state<FileRes[]>([]);

	let showDeleteDialog = $state(false);
	let showTraineeModal = $state(false);
	let showFileModal = $state(false);

	const dateFormat = new Intl.DateTimeFormat($locale || window.navigator.language, {
		year: 'numeric',
		month: '2-digit',
		day: '2-digit',
		hour: '2-digit',
		minute: '2-digit'
	});

	function handleSelectedTrainees(uuids: string[]) {
		// const updatedLearningKit = await api(fetch)
		// 	.req(updateLearningKit, {
		// 		...learningKit,
		// 		learningKitId: learningKit.uuid,
		// 		participants: uuids,
		// 		files: learningKit.files?.map((file) => file.uuid) ?? []
		// 	})
		// 	.parse();
		// await invalidate('learningkits:list');
		// selectedTrainees = updatedLearningKit.participants ?? [];
	}

	function removeTrainee(uuid: string) {
		// handleSelectedTrainees(
		// 	selectedTrainees.filter((trainee) => trainee.uuid != uuid).map((trainee) => trainee.uuid)
		// );
	}

	function handleSelectedFiles(uuids: string[]) {
		// const updatedLearningKit = await api(fetch)
		// 	.req(updateLearningKit, {
		// 		...learningKit,
		// 		learningKitId: learningKit.uuid,
		// 		participants: learningKit.participants?.map((participant) => participant.uuid) ?? [],
		// 		files: uuids
		// 	})
		// 	.parse();
		// await invalidate('learningkits:list');
		// selectedFiles = updatedLearningKit.files ?? [];
	}
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
			<!-- {#each selectedTrainees as trainee (trainee.uuid)}
				<TraineeDisplay
					user={trainee}
					onRemoveTrainee={async () => await removeTrainee(trainee.uuid)}
				/>
			{/each} -->

			<button
				type="button"
				class="btn preset-outlined-surface-500 w-full"
				onclick={() => (showTraineeModal = true)}
			>
				<UserRoundPlus></UserRoundPlus>
				{$_('trainee.add')}
			</button>
		</div>

		<!-- Context -->
		<p class="text-primary-500 mt-5 text-sm font-semibold">{$_('learningKit.context')}</p>
		<p class="mt-5 text-sm">{$_('learningKit.context.description')}</p>
		<div class="flex flex-col gap-2">
			{#each selectedFiles as file (file.uuid)}
				<FileDisplay File={file} />
			{/each}

			<button
				type="button"
				class="btn preset-outlined-surface-500 w-full"
				onclick={() => (showFileModal = true)}
			>
				<Upload></Upload>
				{$_('learningKit.addFile')}
			</button>
			<FileUpload />
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

	<TraineeSelectModal
		isOpen={showTraineeModal}
		onSelect={(selectedTrainees) => {
			// await handleSelectedTrainees(selectedTrainees);
			showTraineeModal = false;
		}}
		onClose={() => (showTraineeModal = false)}
		allTrainees={$learningKitQuery.data.participants}
		selectedParticipants={[]}
	/>

	<FileSelectModal
		isOpen={showFileModal}
		onSelect={(selectedFiles) => {
			handleSelectedFiles(selectedFiles);
			showFileModal = false;
		}}
		onClose={() => (showFileModal = false)}
	/>

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
