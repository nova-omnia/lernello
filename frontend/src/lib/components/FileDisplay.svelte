<script lang="ts">
    import { File,Download  } from 'lucide-svelte';
    //TODO: add dynamic name for file
</script>

<div class="File-Display flex items-center rounded-lg border-2 rounded-border-gray-200 p-3 text-base">
    <File class="w-10 h-10"/>
    <p class="ml-2 text-base text-bold text-black-700">File Name</p>
    <button type="button" class="btn preset-outlined-surface-500 bg-grey rounded-xl ml-auto p-2"><Download /></button>
    <button type="button" class="btn preset-filled-error-500 rounded-xl ml-1 p-2">Remove</button>
</div>