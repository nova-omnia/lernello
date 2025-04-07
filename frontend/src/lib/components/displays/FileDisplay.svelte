<script lang="ts">
	import { File, Download } from 'lucide-svelte';
	//TODO: add dynamic name for file
</script>

<div
	class="File-Display preset-filled-surface-100-900 rounded-border border-surface-200-800 flex items-center rounded-lg border-[1px] p-3 text-base"
>
	<File class="h-10 w-10" />
	<p class="text-bold text-black-700 ml-2 text-base">File Name</p>
	<button type="button" class="btn preset-outlined-surface-500 bg-gray ml-auto rounded-full p-2"
		><Download /></button
	>
	<button type="button" class="btn preset-filled-error-500 ml-1 rounded-full p-2">Remove</button>
</div>
