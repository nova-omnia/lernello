<script lang="ts">
	import { Clock, Settings, Plus, UserRoundPlus } from 'lucide-svelte';
	import LearningUnitDisplay from '$lib/components/displays/LearningUnitDisplay.svelte';
	import CheckpointDisplay from '$lib/components/displays/CheckpointDisplay.svelte';
	import TraineeDisplay from '$lib/components/displays/TraineeDisplay.svelte';
	import FileUpload from '$lib/components/FileUpload.svelte';
	import TraineeSelectModal from '$lib/components/dialogs/TraineeSelectModal.svelte';
	import FileSelectModal from '$lib/components/dialogs/FileSelectModal.svelte';
	import { Upload } from 'lucide-svelte';
	import FileDisplay from '$lib/components/displays/FileDisplay.svelte';
	import { browserApiClient } from '$lib/api/browserApiClient';
	import { deleteLearningKit } from '$lib/api/collections/learningKit';
	import { goto, invalidate } from '$app/navigation';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';

	let { data } = $props();
	const learningKit = data.kitToDisplay;

	const learningUnits = $state(data.kitToDisplay.learningUnits || []);
	let selectedTrainees = $state<
		{ uuid: string; username: string; name: string; surname: string }[]
	>([]);
	let selectedFiles = $state<{ uuid: string; name: string }[]>([]);
	let showDeleteDialog = $state(false);

	let showTraineeModal = $state(false);
	let showFileModal = $state(false);

	function formatDate(date: Date): string {
		const day = String(date.getDate()).padStart(2, '0');
		const month = String(date.getMonth() + 1).padStart(2, '0'); // months are 0-based
		const year = date.getFullYear();
		return `${day}.${month}.${year}`;
	}

	function handleSelectedTrainees(uuids: string[]) {
		// make call to backend to add selected trainees to the learning kit
		//await browserApiClient.req(learningUnitUpdate, {trainees: uuids}, learningKit.uuid)
		// await invalidate('learningkits:list');
	}

	async function handleSelectedFiles(uuids: string[]) {
		// make call to backend to add selected files to the learning kit
		//await browserApiClient.req(learningUnitUpdate, {files: uuids}, learningKit.uuid)
		// await invalidate('learningkits:list');
	}

	async function handleConfirmDelete() {
		if (!learningKit) return;

		await browserApiClient.req(deleteLearningKit, null, learningKit.uuid);
		await invalidate('learningkits:list');

		showDeleteDialog = false;
		await goto('/dashboard');
	}
</script>

<div class="bg-surface-50-950 p-4">
	<!--header-->
	<div class="space-between flex items-start p-1">
		<div>
			<h1 class="text-2xl font-bold">Learning Kit: {learningKit.name}</h1>
			<h2 class="mt-2 text-lg font-semibold">{learningKit.description}</h2>
			{#if learningKit.deadlineDate}
				<p class="mt-2 flex items-center">
					<Clock class="mr-2 inline-block" />
					{formatDate(new Date(learningKit.deadlineDate))}
				</p>
			{/if}
		</div>

		<button type="button" class="btn preset-outlined-surface-500 ml-auto rounded-full p-2">
			<Settings />
			Edit
		</button>
	</div>

	<!--content-->
	<p class="text-primary-500 mt-5 text-sm font-semibold">Content</p>
	<p class="mt-5 text-sm">The contents of the LearningKit</p>
	<!-- foreach learningUnit-->
	{#each learningUnits as learningUnit (learningUnit.uuid)}
		<LearningUnitDisplay {learningUnit} />
	{/each}
	<CheckpointDisplay />
	<button type="button" class="btn preset-outlined-surface-500 ml-auto w-full rounded-xl p-2">
		<Plus></Plus>
		Create new Learning Kit
	</button>

	<!-- trainees -->
	<p class="text-primary-500 mt-5 text-sm font-semibold">Trainees</p>
	<p class="mt-5 text-sm">These Trainees have access to the course</p>

	<div class="flex flex-col gap-2">
		{#each selectedTrainees as trainee (trainee.uuid)}
			<TraineeDisplay User={trainee} />
		{/each}

		<button
			type="button"
			class="btn preset-outlined-surface-500 ml-auto w-full rounded-xl p-2"
			onclick={() => (showTraineeModal = true)}
		>
			<UserRoundPlus></UserRoundPlus>
			Add Trainee
		</button>
	</div>

	<!-- Context -->
	<p class="text-primary-500 mt-5 text-sm font-semibold">Context</p>
	<p class="mt-5 text-sm">The context provided to the AI assisting tools</p>
	<div class="flex flex-col gap-2">
		{#each selectedFiles as file (file.uuid)}
			<FileDisplay File={file} />
		{/each}

		<button
			type="button"
			class="btn preset-outlined-surface-500 ml-auto w-full rounded-xl p-2"
			onclick={() => (showFileModal = true)}
		>
			<Upload></Upload>
			Add File
		</button>
		<FileUpload />
	</div>

	<p class="text-primary-500 mt-5 text-sm font-semibold">Settings</p>
	<p class="mt-5 text-sm">Make changes to the learning kit</p>
	<div class="flex gap-2">
		<button type="button" class="btn preset-filled-primary-500 rounded-full p-2">Publish</button>
		<button
			onclick={(e) => {
				e.preventDefault();
				showDeleteDialog = true;
			}}
			type="button"
			class="btn preset-filled-error-500 rounded-full p-2"
			>Delete learning kit
		</button>
	</div>
</div>

<TraineeSelectModal
	isOpen={showTraineeModal}
	onSelect={(selectedTrainees) => {
		handleSelectedTrainees(selectedTrainees);
		showTraineeModal = false;
	}}
	onClose={() => (showTraineeModal = false)}
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
	message={`Are you sure you want to delete "${learningKit?.name}"?`}
	confirmText="Delete"
	danger={true}
	onConfirm={handleConfirmDelete}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
