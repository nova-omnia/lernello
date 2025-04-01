<script lang="ts">
	import { File, Download } from 'lucide-svelte';
	//TODO: add dynamic name for file
</script>

<div
	class="File-Display rounded-border-gray-200 flex items-center rounded-lg border-2 p-3 text-base"
>
	<File class="h-10 w-10" />
	<p class="text-bold text-black-700 ml-2 text-base">File Name</p>
	<button type="button" class="btn preset-outlined-surface-500 bg-grey ml-auto rounded-xl p-2"
		><Download /></button
	>
	<button type="button" class="btn preset-filled-error-500 ml-1 rounded-xl p-2">Remove</button>
</div>
