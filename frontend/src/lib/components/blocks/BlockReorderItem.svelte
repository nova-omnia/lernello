<script lang="ts">
	import { dragHandle } from 'svelte-dnd-action';
	import { BookOpen, FileText, GripVertical } from 'lucide-svelte';
	import type { Block } from '$lib/models/block';

	export let block: Block;

	let IconComponent = block.type === 'theory' ? BookOpen : FileText;
</script>

<div
	class="preset-filled-surface-100-900 hover:preset-filled-surface-50-950 border-surface-200-800 group relative rounded-lg border p-2 transition-all duration-200"
>
	<div use:dragHandle aria-label={`drag-handle for ${block.name}`} class="cursor-move">
		<GripVertical class="text-surface-400-600 absolute left-2 top-1/2  h-6 w-6 -translate-y-1/2" />
	</div>

	<div class="flex h-full items-center pl-10">
		<IconComponent class="text-surface-400-600 h-6 w-6" />
		<h3 class="ml-2 font-medium">{block.name}</h3>
		<span class="text-surface-400-600 ml-2 h-6 w-6 text-sm">({block.type})</span>
	</div>
</div>
