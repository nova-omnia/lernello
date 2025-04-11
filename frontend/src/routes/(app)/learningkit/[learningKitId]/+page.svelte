<script lang="ts">
	import { Clock, Settings, Plus, UserRoundPlus } from 'lucide-svelte';
	import LearningUnitDisplay from '$lib/components/displays/LearningUnitDisplay.svelte';
	import CheckpointDisplay from '$lib/components/displays/CheckpointDisplay.svelte';
	import TraineeDisplay from '$lib/components/displays/TraineeDisplay.svelte';
	import FileUpload from '$lib/components/FileUpload.svelte';
	import TraineeSelectModal from '$lib/components/dialogs/TraineeSelectModal.svelte';
	import FileSelectModal from '$lib/components/dialogs/FileSelectModal.svelte';
	import { Upload } from 'lucide-svelte';
	import { writable } from 'svelte/store';
	import FileDisplay from '$lib/components/displays/FileDisplay.svelte';

	let { data } = $props();
	const learningKit = data.kitToDisplay;

	const learningUnits = writable(data.kitToDisplay.learningUnits || []);
	let selectedTrainees = $state<{ uuid: string; username: string; name: string; surname: string }[]>([]);
	let selectedFiles = $state<{ uuid: string; name: string }[]>([]);
	
	const showTraineeModal = writable(false);
	const showFileModal = writable(false);

	function formatDate(date: Date): string {
		const day = String(date.getDate()).padStart(2, '0');
		const month = String(date.getMonth() + 1).padStart(2, '0'); // months are 0-based
		const year = date.getFullYear();
		return `${day}.${month}.${year}`;
	}


	// Dummy data for files
	let availableFiles = [
    { uuid: '1', name: 'File 1' },
    { uuid: '2', name: 'File 2' },
    { uuid: '3', name: 'File 3' },
    { uuid: '4', name: 'File 4' },
    { uuid: '5', name: 'File 5' },
    { uuid: '6', name: 'File 6' },
    { uuid: '7', name: 'File 7' },
    { uuid: '8', name: 'File 8' },
    { uuid: '9', name: 'File 9' },
    { uuid: '10', name: 'File 10' },
    { uuid: '11', name: 'File 11' },
    { uuid: '12', name: 'File 12' },
    { uuid: '13', name: 'File 13' },
    { uuid: '14', name: 'File 14' },
    { uuid: '15', name: 'File 15' }
];

	// Dummy data for trainees
	let allTrainees = [
		{ uuid: '1', username: 'john.doe@example.com', name: 'Garbor', surname: 'John' },
		{ uuid: '2', username: 'jane.doe@example.com', name: 'Doedor', surname: 'Jane' },
		{ uuid: '3', username: 'jane.doe@example.com', name: 'Max', surname: 'Sander' },
		{ uuid: '4', username: 'jane.doe@example.com', name: 'Bea', surname: 'Tilder' },
		{ uuid: '5', username: 'jane.doe@example.com', name: 'Tony', surname: 'Blair' },
		{ uuid: '6', username: 'jane.doe@example.com', name: 'John', surname: 'Clark' },
		{ uuid: '7', username: 'jane.doe@example.com', name: 'Adam', surname: 'Smith' },
		{ uuid: '8', username: 'jane.doe@example.com', name: 'Friz', surname: 'Taylor' },
		{ uuid: '9', username: 'jane.doe@example.com', name: 'Sasha', surname: 'Leon' },
		{ uuid: '10', username: 'jane.doe@example.com', name: 'Selina', surname: 'Brown' },
		{ uuid: '11', username: 'jane.doe@example.com', name: 'Carola', surname: 'Brown' },
		{ uuid: '12', username: 'jane.doe@example.com', name: 'Tom', surname: 'Frisch' }
	];

	function handleSelectedTrainees(uuids: string[]) {
		selectedTrainees = allTrainees.filter((trainee) => uuids.includes(trainee.uuid));
		// make call to backend to add selected trainees to the learning kit
	}

	function handleSelectedFiles(uuids: string[]) {
		selectedFiles = availableFiles.filter((file) => uuids.includes(file.uuid));
		// make call to backend to add selected files to the learning kit
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

		<button type="button" class="btn preset-outlined-surface-500 ml-auto rounded-full p-2"
			><Settings />Edit</button
		>
	</div>

	<!--content-->
	<p class="text-primary-500 mt-5 text-sm font-semibold">Content</p>
	<p class="mt-5 text-sm">The contents of the LearningKit</p>
	<!-- foreach learningUnit-->
	{#each $learningUnits as learningUnit}
		<LearningUnitDisplay {learningUnit} />
	{/each}
	<CheckpointDisplay />
	<button type="button" class="btn preset-outlined-surface-500 ml-auto w-full rounded-xl p-2"
		><Plus></Plus>Create new Learning Kit</button
	>

	<!-- trainees -->
	<p class="text-primary-500 mt-5 text-sm font-semibold">Trainees</p>
	<p class="mt-5 text-sm">These Trainees have access to the course</p>

	<div class="flex flex-col gap-2">
		{#each selectedTrainees as trainee}
			<TraineeDisplay User={trainee} />
		{/each}

		<button
			type="button"
			class="btn preset-outlined-surface-500 ml-auto w-full rounded-xl p-2"
			onclick={() => showTraineeModal.set(true)}
		>
			<UserRoundPlus></UserRoundPlus>Add Trainee
		</button>
	</div>

	<!-- Context -->
	<p class="text-primary-500 mt-5 text-sm font-semibold">Context</p>
	<p class="mt-5 text-sm">The context provided to the AI assisting tools</p>
	<div class="flex flex-col gap-2">
		{#each selectedFiles as file}
			<FileDisplay File={file} />
		{/each}

		<button
			type="button"
			class="btn preset-outlined-surface-500 ml-auto w-full rounded-xl p-2"
			onclick={() => showFileModal.set(true)}
		>
			<Upload></Upload>Add File
		</button>
		<FileUpload />
	</div>

	<p class="text-primary-500 mt-5 text-sm font-semibold">Settings</p>
	<p class="mt-5 text-sm">Make changes to the learning kit</p>
	<div class="flex gap-2">
		<button type="button" class="btn preset-filled-primary-500 rounded-full p-2">Publish</button>
		<button type="button" class="btn preset-filled-error-500 rounded-full p-2"
			>Delete learning kit</button
		>
	</div>
</div>

<TraineeSelectModal
	open={$showTraineeModal}
	trainees={allTrainees}
	onSelect={(e) => {
		handleSelectedTrainees(e);
		showTraineeModal.set(false);
	}}
	onClose={() => showTraineeModal.set(false)}
/>

<FileSelectModal
	open={$showFileModal}
	files={availableFiles}
	onSelect={(e) => {
		handleSelectedFiles(e);
		showFileModal.set(false);
	}}
	onClose={() => showFileModal.set(false)}
/>
