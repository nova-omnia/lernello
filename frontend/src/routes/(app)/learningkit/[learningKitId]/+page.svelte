<script lang="ts">
	import MultiSelect from '$lib/components/MultiSelect.svelte';
	import { Clock, Settings, Plus, Upload } from 'lucide-svelte';
	import LearningUnitDisplay from '$lib/components/displays/LearningUnitDisplay.svelte';
	import CheckpointDisplay from '$lib/components/displays/CheckpointDisplay.svelte';
	import TraineeDisplay from '$lib/components/displays/TraineeDisplay.svelte';
	import FileUpload from '$lib/components/FileUpload.svelte';
	import FileDisplay from '$lib/components/displays/FileDisplay.svelte';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';
	import { _ } from 'svelte-i18n';
	import { goto, invalidate } from '$app/navigation';
	import { api } from '$lib/api/apiClient.js';
	import { updateLearningKit } from '$lib/api/collections/learningKit.js';
	import { deleteLearningKit } from '$lib/api/collections/learningKit';
	import { deleteLearningUnit } from '$lib/api/collections/learningUnit.js';
	import type { ParticipantUser } from '$lib/schemas/response/ParticipantUser';
	import type { FileRes } from '$lib/schemas/response/FileRes';

	let { data } = $props();
	const learningKit = data.kitToDisplay;

	let learningUnits = $state(data.kitToDisplay.learningUnits || []);
	let selectedTrainees = $state<ParticipantUser[]>(data.kitToDisplay.participants ?? []);
	let selectedFiles = $state<FileRes[]>(data.kitToDisplay.files ?? []);
	let showDeleteDialog = $state(false);

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
				participants: learningKit.participants?.map((p) => p.uuid) ?? [],
				files: uuids
			})
			.parse();
		await invalidate('learningkits:list');
		selectedFiles = updatedLearningKit.files ?? [];
	}

	async function removeFile(uuid: string) {
		handleSelectedFiles(selectedFiles.filter((file) => file.uuid != uuid).map((file) => file.uuid));
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
		learningUnits = learningUnits?.filter((lu) => lu.uuid != learningUnitId);
	}
</script>

<div class="bg-surface-50-950 p-4">
	<!-- Header -->
	<div class="space-between flex items-start justify-between p-1">
		<div>
			<h1 class="text-2xl font-bold">{$_('learningKit.title')}: {learningKit.name}</h1>
			<h2 class="mt-2 text-lg font-semibold">{learningKit.description}</h2>
			{#if learningKit.deadlineDate}
				<p class="mt-2 flex items-center">
					<Clock class="mr-2 inline-block" />
					{new Date(learningKit.deadlineDate).toLocaleDateString()}
				</p>
			{/if}
		</div>

		<button type="button" class="btn preset-outlined-surface-500 rounded-full">
			<Settings />
			{$_('button.edit')}
		</button>
	</div>

	<!-- Content -->
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
		<Plus />
		{$_('learningUnit.create')}
	</button>

	<!-- Trainees -->
	<p class="text-primary-500 mt-5 text-sm font-semibold">{$_('trainee.title')}</p>
	<p class="mt-5 text-sm">{$_('trainee.access')}</p>

	<MultiSelect
		options={data.allTrainees.map((t) => ({
			uuid: t.uuid,
			label: `${t.username} | ${t.name} ${t.surname}`
		}))}
		selected={selectedTrainees.map((t) => ({
			uuid: t.uuid,
			label: `${t.username} | ${t.name} ${t.surname}`
		}))}
		onSelect={async (options) => {
			await handleSelectedTrainees(options.map((o) => o.uuid));
		}}
	/>

	<div class="mt-2 flex flex-col gap-2">
		{#each selectedTrainees as trainee (trainee.uuid)}
			<TraineeDisplay
				user={trainee}
				onRemoveTrainee={async () => await removeTrainee(trainee.uuid)}
			/>
		{/each}
	</div>

	<!-- Context / Files -->
	<p class="text-primary-500 mt-5 text-sm font-semibold">{$_('learningKit.context')}</p>
	<p class="mt-5 text-sm">{$_('learningKit.context.description')}</p>

	<MultiSelect
		options={data.allFiles.map((f) => ({
			uuid: f.uuid,
			label: `${f.name}`
		}))}
		selected={selectedFiles.map((f) => ({
			uuid: f.uuid,
			label: `${f.name}`
		}))}
		onSelect={async (options) => {
			await handleSelectedFiles(options.map((o) => o.uuid));
		}}
	/>

	<div class="mt-2 flex flex-col gap-2">
		{#each selectedFiles as file (file.uuid)}
			<FileDisplay File={file} onRemoveFile={async () => await removeFile(file.uuid)} />
		{/each}
		<FileUpload />
	</div>

	<!-- Settings -->
	<p class="text-primary-500 mt-5 text-sm font-semibold">{$_('learningKit.settings')}</p>
	<p class="mt-5 text-sm">{$_('learningKit.settings.change')}</p>
	<div class="flex gap-2">
		<button type="button" class="btn preset-filled-primary-500 rounded-full">
			{$_('learningKit.publish')}
		</button>
		<button
			onclick={(e) => {
				e.preventDefault();
				showDeleteDialog = true;
			}}
			type="button"
			class="btn preset-filled-error-500 rounded-full"
		>
			{$_('learningKit.delete')}
		</button>
	</div>
</div>

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
