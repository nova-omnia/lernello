<script lang="ts">
	import { ParticipantUser } from './../../../../lib/schemas/response/ParticipantUser.ts';
	import { Clock, Settings, Plus, UserRoundPlus } from 'lucide-svelte';
	import LearningUnitDisplay from '$lib/components/displays/LearningUnitDisplay.svelte';
	import CheckpointDisplay from '$lib/components/displays/CheckpointDisplay.svelte';
	import TraineeDisplay from '$lib/components/displays/TraineeDisplay.svelte';
	import FileUpload from '$lib/components/FileUpload.svelte';
	import TraineeSelectModal from '$lib/components/dialogs/TraineeSelectModal.svelte';
	import FileSelectModal from '$lib/components/dialogs/FileSelectModal.svelte';
	import { Upload } from 'lucide-svelte';
	import FileDisplay from '$lib/components/displays/FileDisplay.svelte';
	import { deleteLearningKit } from '$lib/api/collections/learningKit';
	import { goto, invalidate } from '$app/navigation';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _ } from 'svelte-i18n';
	import { updateLearningKit } from '$lib/api/collections/learningKit.js';
	import type { ParticipantUser } from '$lib/schemas/response/ParticipantUser';
	import type { FileRes } from '$lib/schemas/response/FileRes';
	import { deleteLearningUnit } from '$lib/api/collections/learningUnit.js';
	import { api } from '$lib/api/apiClient.js';
	import MultiSelect from '$lib/components/MultiSelect.svelte';
	import { handle } from '../../../../hooks.server.js';
	import { getAllTrainees } from '$lib/api/collections/user.js';

	let { data } = $props();
	const learningKit = data.kitToDisplay;

	let learningUnits = $state(data.kitToDisplay.learningUnits || []);
	let selectedTrainees = $state<ParticipantUser[]>(data.kitToDisplay.participants ?? []);
	let selectedFiles = $state<FileRes[]>([]);
	let showDeleteDialog = $state(false);

	let showTraineeModal = $state(false);
	let showFileModal = $state(false);

	function formatDate(date: Date): string {
		const day = String(date.getDate()).padStart(2, '0');
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const year = date.getFullYear();
		return `${day}.${month}.${year}`;
	}

	async function handleSelectedTrainees(uuids: string[]) {
		const updatedLearningKit = await api(fetch)
			.req(updateLearningKit, {
				...learningKit,
				learningKitId: learningKit.uuid,
				participants: uuids,
				files: learningKit.files?.map((file) => file.uuid) ?? []
			})
			.parse();
		await invalidate('learningkits:list');
		selectedTrainees = updatedLearningKit.participants ?? [];
	}

	async function removeTrainee(uuid: string) {
		handleSelectedTrainees(
			selectedTrainees.filter((trainee) => trainee.uuid != uuid).map((trainee) => trainee.uuid)
		);
	}

	async function handleSelectedFiles(uuids: string[]) {
		const updatedLearningKit = await api(fetch)
			.req(updateLearningKit, {
				...learningKit,
				learningKitId: learningKit.uuid,
				participants: learningKit.participants?.map((participant) => participant.uuid) ?? [],
				files: uuids
			})
			.parse();
		await invalidate('learningkits:list');
		selectedFiles = updatedLearningKit.files ?? [];
	}

	async function handleCreateNewLearningUnit() {
		await goto(`../../learningunit/create-form?learningKitId=${learningKit.uuid}`);
	}

	async function handleConfirmDelete() {
		if (!learningKit) return;

		await api(fetch).req(deleteLearningKit, null, learningKit.uuid).parse();
		await invalidate('learningkits:list');

		showDeleteDialog = false;
		await goto('/dashboard');
	}

	async function deleteLearningUnitFromKit(learningUnitId: string) {
		await api(fetch).req(deleteLearningUnit, null, learningUnitId).parse();
		learningUnits = learningUnits?.filter((learningUnit) => learningUnit.uuid != learningUnitId);
	}
</script>

<div class="bg-surface-50-950 p-4">
	<!--header-->
	<div class="space-between flex items-start justify-between p-1">
		<div>
			<h1 class="text-2xl font-bold">{$_('learningKit.title')}: {learningKit.name}</h1>
			<h2 class="mt-2 text-lg font-semibold">{learningKit.description}</h2>
			{#if learningKit.deadlineDate}
				<p class="mt-2 flex items-center">
					<Clock class="mr-2 inline-block" />
					{formatDate(new Date(learningKit.deadlineDate))}
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
		{#each learningUnits as learningUnit (learningUnit.uuid)}
			<LearningUnitDisplay
				{learningUnit}
				onDeleteLearningUnit={async () => {
					deleteLearningUnitFromKit(learningUnit.uuid);
				}}
			/>
		{/each}
		<CheckpointDisplay />
	</div>
	<button
		type="button"
		class="btn preset-outlined-surface-500 w-full"
		onclick={() => handleCreateNewLearningUnit()}
	>
		<Plus></Plus>
		{$_('learningUnit.create')}
	</button>

	<!-- trainees -->
	<p class="text-primary-500 mt-5 text-sm font-semibold">{$_('trainee.title')}</p>
	<p class="mt-5 text-sm">{$_('trainee.access')}</p>

	<div class="flex flex-col gap-2">
		{#each selectedTrainees as trainee (trainee.uuid)}
			<TraineeDisplay
				user={trainee}
				onRemoveTrainee={async () => await removeTrainee(trainee.uuid)}
			/>
		{/each}

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
<MultiSelect
	placeholder={$_('multiSelect.selectTrainees')}
	options={ParticipantUser}
	onSelect={async (selectedTrainees) => {
		const selectedUUIDs = selectedOptions.map((option) => option.value);
		await handleSelectedTrainees(selectedUUIDs);
		}}
	selected={selectedTrainees}
/>

<TraineeSelectModal
	isOpen={showTraineeModal}
	onSelect={async (selectedTrainees) => {
	
		await handleSelectedTrainees(selectedTrainees);
		showTraineeModal = false;
	}}
	onClose={() => (showTraineeModal = false)}
	allTrainees={data.allTrainees}
	selectedParticipants={selectedTrainees}
/>

<FileSelectModal
	isOpen={showFileModal}
	onSelect={async (selectedFiles) => {
		await handleSelectedFiles(selectedFiles);
		showFileModal = false;
	}}
	onClose={() => (showFileModal = false)}
	selectedFileUUIDs={selectedFiles.map((file) => file.uuid)}
/>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title="Confirm Deletion"
	message={`Are you sure you want to delete "${learningKit?.name}"?`}
	confirmText="Delete"
	danger={true}
	onConfirm={handleConfirmDelete}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
