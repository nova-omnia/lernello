<script lang="ts">
	import TheoryBlockComponent from './theoryBlockComponent.svelte';
	import QuizBlockComponent from './quizBlockComponent.svelte';

	type BlockType = { uuid: string; name: string; type: string };

	export let block: BlockType;
	export let onDelete: (block: BlockType) => void;

	let Component = block.type === 'theory' ? TheoryBlockComponent : QuizBlockComponent;

	let icon = block.type === 'theory' ? 'open-book' : 'pencil-file';

	function handleDelete() {
		onDelete(block);
	}
</script>

<div
	class="group hover:border-primary-500 relative rounded-lg border border-gray-200 bg-white p-4 transition-all duration-200"
>
	<!-- Delete Button -->
	<button
		class="absolute -top-2 -right-2 rounded-full bg-red-500 p-1 text-white opacity-0 transition-opacity group-hover:opacity-100"
		on:click={handleDelete}
	>
		<svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
			<use href="/app-icons/x-icon.svg#x-icon" />
		</svg>
	</button>


	<!-- Block Header -->
	<div class="flex items-center pl-6 space-x-2">
		<svg
				xmlns="http://www.w3.org/2000/svg"
				class="h-6 w-6 text-gray-500"
				viewBox="0 0 20 20"
				fill="currentColor"
		>
			<use href="/app-icons/{icon}-icon.svg#{icon}-icon" />
		</svg>
		<h3 class="font-medium">{block.name}</h3>
		<span class="ml-2 text-sm text-gray-500">({block.type})</span>
	</div>
	<!-- Render the block content component -->
	<svelte:component this={Component} />
</div>
