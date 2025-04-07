<script lang="ts">
	import FileDisplay from '$lib/components/FileDisplay.svelte';
	import FileUpload from '$lib/components/FileUpload.svelte';
	import TraineeList from '$lib/components/TraineeList.svelte';
</script>

<div class="ml-4">
	<h1 class="font-bold">LK1</h1>
	<p>Description</p>
	<div class="mt-5">
		<h2 class="mb-2 text-lg font-semibold">Upload Files</h2>
		<FileUpload />
		<TraineeList />
		<FileDisplay />
	</div>
</div>
