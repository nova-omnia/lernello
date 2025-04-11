<script lang="ts">
	import { GripVertical, Lock } from 'lucide-svelte';

    function deleteCheckpoint() {
        //todo
    }

</script>

<div class="flex items-center p-1">
	<GripVertical color="gray" class="h-10 w-10" />
	<div
		class="Learning-Unit-Display preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center rounded-lg border-[1px] p-3 text-base"
	>
		<div class="flex items-start">
			<Lock class="h-10 w-10" />
			<div class="ml-2 flex flex-col justify-center">
				<p class="text-black-700 text-xs font-bold">Checkpoint</p>
				<p class="text-xs text-gray-700">
					Trainees must complete all the above Learning Units, before gaining access to the rest.
				</p>
			</div>
		</div>

		<button
			type="button"
			class="btn preset-filled-error-500 ml-auto rounded-full p-2"
			on:click={deleteCheckpoint}>Delete</button
		>
	</div>
</div>
