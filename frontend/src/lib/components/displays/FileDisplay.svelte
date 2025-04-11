<script lang="ts">
	import { File as FileIcon } from 'lucide-svelte';
	const { File } = $props();
	function removeFile(event: MouseEvent & { currentTarget: EventTarget & HTMLButtonElement; }) {
		//todo
	}
</script>

<div
	class="File-Display preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center rounded-lg border-[1px] p-3 text-base"
>
	<FileIcon class="h-10 w-10" />
	<p class="text-black-700 ml-3 text-xs font-bold">{File.name}</p>
	<button type="button" class="btn preset-filled-error-500 ml-auto rounded-full p-2">Remove</button>
</div>

