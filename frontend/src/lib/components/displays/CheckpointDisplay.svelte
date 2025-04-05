<script lang="ts">
    import { GripVertical, Lock  } from 'lucide-svelte';
</script>
<div class="flex items-center p-1">
    <GripVertical color="gray" class="h-10 w-10" />
    <div
            class="Learning-Unit-Display preset-filled-surface-100-900 rounded-border border-surface-200-800 flex items-center rounded-lg border-[1px] p-3 text-base w-full"
    >
        <div class="flex items-start">
            <Lock class="h-10 w-10" />
            <div class="ml-2 flex flex-col justify-center">
                <p class="font-bold text-black-700 text-xs">Checkpoint</p>
                <p class="text-gray-700 text-xs">Trainees must complete all the above Learning Units, before gaining access to the rest.</p>
            </div>
        </div>

        <button type="button" class="btn preset-filled-error-500 ml-auto rounded-full p-2">Delete</button>
    </div>
</div>