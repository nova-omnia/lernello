<script lang="ts">
	let { data } = $props();
	const kit = data.kitToDisplay;

	const dateFormatter = new Intl.DateTimeFormat('de-DE', {
		year: 'numeric',
		month: '2-digit',
		day: '2-digit',
		hour: '2-digit',
		minute: '2-digit'
	});
</script>

<div class="p-5">
	<h1 class="text-2xl font-bold">Learning Kit: {kit?.name}</h1>
	<p class="mt-2">Description: {kit?.description}</p>
	{#if kit?.deadlineDate}
		<p class="mt-2">
			Deadline: {dateFormatter.format(new Date(kit.deadlineDate))}
		</p>
	{/if}
</div>
