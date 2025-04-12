<script lang="ts">
	import { AlignLeft, GripVertical } from 'lucide-svelte';
	import { browserApiClient } from '$lib/api/browserApiClient.js';
	import {
		getLearningUnitById,
		deleteLearningUnit,
		regenerateLearningUnit
	} from '$lib/api/collections/learningUnit';
	import { goto, invalidate } from '$app/navigation';
	import ConfirmDialog from '$lib/components/dialogs/ConfirmDialog.svelte';

	let showDeleteDialog = $state(false);

	const { learningUnit } = $props();

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
		if (!learningUnit) return;

		await browserApiClient.req(deleteLearningUnit, null, learningUnit.uuid);
		await invalidate('learningunits:list');

		showDeleteDialog = false;
	}
</script>

<div class="flex items-center p-1">
	<GripVertical color="gray" class="h-10 w-10" />
	<div
		class="Learning-Unit-Display preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center rounded-lg border-[1px] p-3 text-base"
	>
		<div class="flex items-start">
			<AlignLeft class="h-10 w-10" />
			<div class="ml-2 flex flex-col justify-center">
				<p class="text-black-700 text-xs font-bold">{learningUnit.name}</p>
				<p class="text-xs text-gray-700">{learningUnit.description}</p>
			</div>
		</div>

		<button
			type="button"
			onclick={openLearningUnit}
			class="btn preset-filled-primary-500 ml-auto rounded-full p-2">Open</button
		>
		<button
			type="button"
			onclick={regenerateLearningUnitHandler}
			class="btn preset-outlined-surface-500 bg-gray ml-1 rounded-full p-2">⚡Regenerate</button
		>
		<button
			type="button"
			onclick={(e) => {
				e.preventDefault();
				showDeleteDialog = true;
			}}
			class="btn preset-filled-error-500 ml-1 rounded-full p-2">Delete</button
		>
	</div>
</div>

<ConfirmDialog
	isOpen={showDeleteDialog}
	title="Confirm Deletion"
	message={`Are you sure you want to delete "${learningUnit?.name}"?`}
	confirmText="Delete"
	danger={true}
	onConfirm={deleteLearningUnitHandler}
	onCancel={() => {
		showDeleteDialog = false;
	}}
/>
