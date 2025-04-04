<script lang="ts">
    import { AlignLeft, GripVertical } from 'lucide-svelte';
    //TODO: add dynamic name for file
</script>
<div class="flex items-center gap-2">
    <GripVertical class="h-10 w-10" />
    <div
            class="Learning-Unit-Display preset-filled-surface-100-900 rounded-border border-surface-200-800 flex items-center rounded-lg border-[1px] p-3 text-base w-full"
    >
        <div class="flex items-start">
            <AlignLeft class="h-10 w-10" />
            <div class="ml-2 flex flex-col justify-center">
                <p class="font-bold text-black-700 text-xs">Learning Unit Name</p>
                <p class="text-gray-700 text-xs">Learning Unit Description</p>
            </div>
        </div>

        <button type="button" class="btn preset-filled-primary-500 ml-auto rounded-xl p-2">Open</button>
        <button type="button" class="btn preset-outlined-surface-500 bg-gray ml-1 rounded-xl p-2">⚡Regenerate</button>
        <button type="button" class="btn preset-filled-error-500 ml-1 rounded-xl p-2">Delete</button>
    </div>
</div>