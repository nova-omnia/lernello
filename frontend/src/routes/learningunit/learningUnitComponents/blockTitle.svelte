<script lang="ts">
	import { dragHandle } from 'svelte-dnd-action';

	type BlockType = { uuid: string; name: string; type: string };

	export let block: BlockType;

	let icon = block.type === 'theory' ? 'open-book' : 'pencil-file';
</script>

<div
	class="group relative rounded-lg border border-gray-200 bg-white p-2 transition-all duration-200"
>
	<div use:dragHandle aria-label="drag-handle for {block.name}">
		<svg
			xmlns="http://www.w3.org/2000/svg"
			class="absolute top-1/2 left-2 h-5 w-5 -translate-y-1/2 transform"
			viewBox="0 0 20 20"
			fill="currentColor"
		>
			<use href="/app-icons/drag-handle-icon.svg#drag-handle-icon" />
		</svg>
	</div>

	<div class="flex h-full items-center pl-10">
		<svg
			xmlns="http://www.w3.org/2000/svg"
			class="h-6 w-6 text-gray-500"
			viewBox="0 0 20 20"
			fill="currentColor"
		>
			<use href="/app-icons/{icon}-icon.svg#{icon}-icon" />
		</svg>
		<h3 class="ml-2 font-medium">{block.name}</h3>
		<span class="ml-2 text-sm text-gray-500">({block.type})</span>
	</div>
</div>
