<script lang="ts">
	import { Clock,Settings,Plus } from 'lucide-svelte';
	import LearningUnitDisplay from "$lib/components/displays/LearningUnitDisplay.svelte";
	import CheckpointDisplay from "$lib/components/displays/CheckpointDisplay.svelte";
	let { data } = $props();
	const kit = data.kit;

	function formatDate(date: Date): string {
		const day = String(date.getDate()).padStart(2, '0');
		const month = String(date.getMonth() + 1).padStart(2, '0'); // months are 0-based
		const year = date.getFullYear();
		return `${day}.${month}.${year}`;
	}
	let learningUnit = {
		name: 'test',
		description: 'hushf'
	};
</script>

<div class="p-4 bg-surface-100-900">
	<!--header-->
	<div class="flex space-between items-start p-1">
		<div>
		<h1 class="text-2xl font-bold">Learning Kit: {kit.name}</h1>
		<h2 class="mt-2 text-lg font-semibold">{kit.description}</h2>
		<p class="mt-2 flex items-center"><Clock class="inline-block mr-2" /> {formatDate(kit.deadlineDate)}</p>
		</div>

		<button class="btn preset-outlined-surface-500 ml-auto rounded-full p-2"><Settings/>Edit</button>
	</div>

	<!--content-->
	<p class="mt-5 text-sm text-primary-500 font-semibold">Content</p>
	<p class="mt-5 text-sm font-semibold">The contents of the LearningKit</p>
	<!-- foreach learningUnit-->
	<LearningUnitDisplay {learningUnit}/>
	<CheckpointDisplay />
	<button class="btn preset-outlined-surface-500 ml-auto w-full rounded-xl p-4" ><Plus></Plus>Create new Learning Kit</button>

	<!-- trainee -->
	<p class="mt-5 text-sm text-primary-500 font-semibold">Trainees</p>

</div>