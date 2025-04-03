<script lang="ts">
	let { data } = $props();
	const kit = data.kit;

	function formatDate(date: Date): string {
		const day = String(date.getDate()).padStart(2, '0');
		const month = String(date.getMonth() + 1).padStart(2, '0'); // months are 0-based
		const year = date.getFullYear();
		return `${day}.${month}.${year}`;
	}
</script>

<div class="p-5">
	<h1 class="text-2xl font-bold">Learning Kit: {kit.name}</h1>
	<p class="text-gray-600">Language: {kit.language}</p>
	<p class="mt-2">Description: {kit.description}</p>
	<p class="mt-2">Deadline: {formatDate(kit.deadlineDate)}</p>
</div>
