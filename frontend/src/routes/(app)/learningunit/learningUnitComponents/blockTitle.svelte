<script lang="ts">
	import { dragHandle } from 'svelte-dnd-action';
	import { BookOpen, FileText, GripVertical } from 'lucide-svelte';
	import type { Block } from '$lib/models/globalBlock';

	export let block: Block;

	let IconComponent = block.type === 'theory' ? BookOpen : FileText;
</script>

<div
	class="group relative rounded-lg border border-gray-200 bg-white p-2 transition-all duration-200 hover:bg-gray-50"
>
	<div use:dragHandle aria-label={`drag-handle for ${block.name}`} class="cursor-move">
		<GripVertical class="absolute top-1/2 left-2 h-5 w-5 -translate-y-1/2 text-gray-500" />
	</div>

	<div class="flex h-full items-center pl-10">
		<IconComponent class="h-6 w-6 text-gray-500" />
		<h3 class="ml-2 font-medium">{block.name}</h3>
		<span class="ml-2 text-sm text-gray-500">({block.type})</span>
	</div>
</div>
