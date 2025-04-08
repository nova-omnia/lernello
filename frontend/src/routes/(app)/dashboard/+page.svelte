<script lang="ts">
	import AITheoryBlock from './../../../lib/components/AITheoryBlock.svelte';
</script>

<div class="p-5">
	<p class="preset-typo-subtitle">Good morning!</p>

	<h1 class="h1">Dashboard</h1>
	<!-- <a href="/learningkit" class="card preset-filled-surface-100-900 no-underline hover:underline"
		>Manage Learning Kits</a
	> -->
	<!-- <div class="mt-5 flex gap-5">
		<div class="w-52 rounded-lg border border-gray-300 p-5 text-center">
			<h3 class="my-2.5">LK1</h3>
			<p class="text-gray-600">Description</p>
			<button
				class="cursor-pointer rounded border-none bg-blue-500 px-5 py-2 text-white hover:bg-blue-700"
				>Edit</button
			>
		</div>
		<div class="w-52 rounded-lg border border-gray-300 p-5 text-center">
			<h3 class="my-2.5">LK2</h3>
			<p class="text-gray-600">Description</p>
			<button
				class="cursor-pointer rounded border-none bg-blue-500 px-5 py-2 text-white hover:bg-blue-700"
				>Edit</button
			>
		</div>
	</div> -->
	<div class="mt-5">
		<a href="/learningkit/create-form" class="btn preset-filled-primary-400-600"
			>Create new Learning Kit</a
		>
	</div>
	<div>
		<AITheoryBlock />
	</div>
</div>
