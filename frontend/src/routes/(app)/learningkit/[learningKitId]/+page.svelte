<script lang="ts">
	import { Clock,Settings,Plus, UserRoundPlus } from 'lucide-svelte';
	import LearningUnitDisplay from "$lib/components/displays/LearningUnitDisplay.svelte";
	import CheckpointDisplay from "$lib/components/displays/CheckpointDisplay.svelte";
	import TraineeDisplay from "$lib/components/displays/TraineeDisplay.svelte";
	import FileUpload from "$lib/components/FileUpload.svelte";
	import TraineeSelectModal from "$lib/components/dialogs/TraineeSelectModal.svelte";
	import {writable} from "svelte/store";

	let { data } = $props();
	const learningKit = data.kitToDisplay;

	const learningUnits = learningKit.learningUnits || [];

	function formatDate(date: Date): string {
		const day = String(date.getDate()).padStart(2, '0');
		const month = String(date.getMonth() + 1).padStart(2, '0'); // months are 0-based
		const year = date.getFullYear();
		return `${day}.${month}.${year}`;
	}

	const showModal = writable(false);

	// Dummy data for trainees
	let allTrainees = [
		{ uuid: '1', username: 'john.doe@example.com', name: 'Test', surname: 'John' },
		{ uuid: '2', username: 'jane.doe@example.com', name: 'Doedor', surname: 'Jane' }
	];

	let selectedTrainees = $state<{ uuid: string; username: string; name: string; surname: string }[]>([]);

	function handleSelect(uuids: string[]) {
		selectedTrainees = allTrainees.filter(trainee => uuids.includes(trainee.uuid));
		// make call to backend to add selected trainees to the learning kit
	}

</script>

<div class="p-4 bg-surface-50-950">
	<!--header-->
	<div class="flex space-between items-start p-1">
		<div>
		<h1 class="text-2xl font-bold">Learning Kit: {learningKit.name}</h1>
		<h2 class="mt-2 text-lg font-semibold">{learningKit.description}</h2>
			{#if learningKit.deadlineDate}
				<p class="mt-2 flex items-center"><Clock class="inline-block mr-2" /> {formatDate(learningKit.deadlineDate)}</p>
			{/if}
		</div>

		<button type="button" class="btn preset-outlined-surface-500 ml-auto rounded-full p-2"><Settings/>Edit</button>
	</div>

	<!--content-->
	<p class="mt-5 text-sm text-primary-500 font-semibold">Content</p>
	<p class="mt-5 text-sm">The contents of the LearningKit</p>
	<!-- foreach learningUnit-->
	{#each learningUnits as learningUnit}
		<LearningUnitDisplay {learningUnit}/>
	{/each}
	<CheckpointDisplay/>
	<button type="button" class="btn preset-outlined-surface-500 ml-auto w-full rounded-xl p-2" ><Plus></Plus>Create new Learning Kit</button>

	<!-- trainees -->
	<p class="mt-5 text-sm text-primary-500 font-semibold">Trainees</p>
	<p class="mt-5 text-sm">These Trainees have access to the course</p>

	<div class="flex flex-col gap-2">
		{#each selectedTrainees as trainee}
			<TraineeDisplay User={trainee}/>
		{/each}

		<button type="button"
				class="btn preset-outlined-surface-500 ml-auto w-full rounded-xl p-2"
				on:click={() => showModal.set(true)}>
				<UserRoundPlus></UserRoundPlus>Add Trainee
		</button>
	</div>

	<!-- Context -->
	<p class="mt-5 text-sm text-primary-500 font-semibold">Context</p>
	<p class="mt-5 text-sm">The context provided to the AI assisting tools</p>
	<!-- foreach context-->
	<div class="flex flex-col gap-2">
		<FileUpload />
	</div>

	<p class="mt-5 text-sm text-primary-500 font-semibold">Settings</p>
	<p class="mt-5 text-sm">Make changes to the learning kit</p>
	<div class="flex gap-2">
		<button type="button" class="btn preset-filled-primary-500 rounded-full p-2" >Publish</button>
		<button type="button" class="btn preset-filled-error-500 rounded-full p-2" >Delete learning kit</button>
	</div>

</div>

<TraineeSelectModal
		open={$showModal}
		trainees={allTrainees}
		onSelect={(e) => {
		handleSelect(e);
		showModal.set(false);
	}}
		onClose={() => showModal.set(false)}
/>