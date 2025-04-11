<script lang="ts">
    import { AlignLeft, GripVertical } from 'lucide-svelte';
    import {browserApiClient} from "$lib/api/browserApiClient.js";
    import {getLearningUnitById, deleteLearningUnit, regenerateLearningUnit} from "$lib/api/collections/learningUnit";
    import {goto} from "$app/navigation";

    const {learningUnit} = $props();

    async function openLearningUnit() {
        let unit = await browserApiClient.req(getLearningUnitById, null, learningUnit.uuid);
        console.log(unit);
        console.log(`Current path: ${window.location.pathname}`);
        await goto('../learningunit/' + unit.uuid);
    }

    async function regenerateLearningUnitHandler() {
        await browserApiClient.req(regenerateLearningUnit, null, learningUnit.id);
    }

    async function deleteLearningUnitHandler() {
        await browserApiClient.req(deleteLearningUnit, null, learningUnit.id);
    }

</script>
<div class="flex items-center p-1">
    <GripVertical color="gray" class="h-10 w-10" />
    <div
            class="Learning-Unit-Display preset-filled-surface-100-900 rounded-border border-surface-200-800 flex items-center rounded-lg border-[1px] p-3 text-base w-full"
    >
        <div class="flex items-start">
            <AlignLeft class="h-10 w-10" />
            <div class="ml-2 flex flex-col justify-center">
                <p class="font-bold text-black-700 text-xs"> {learningUnit.name} </p>
                <p class="text-gray-700 text-xs">{learningUnit.description}</p>
            </div>
        </div>

        <button type="button" on:click={openLearningUnit} class="btn preset-filled-primary-500 ml-auto rounded-full p-2">Open</button>
        <button type="button" on:click={regenerateLearningUnitHandler} class="btn preset-outlined-surface-500 bg-gray ml-1 rounded-full p-2">⚡Regenerate</button>
        <button type="button" on:click={deleteLearningUnitHandler} class="btn preset-filled-error-500 ml-1 rounded-full p-2">Delete</button>
    </div>
</div>