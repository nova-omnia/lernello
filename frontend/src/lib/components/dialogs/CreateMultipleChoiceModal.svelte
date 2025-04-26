<script lang="ts">
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';

	interface MultipleChoiceModalProps {
		isOpen: boolean;
		onConfirm: (selectedBlockId: string) => void;
		onCancel: () => void;
		theoryBlocks: { id: string; title: string }[];
	}

	const { isOpen = false, onConfirm, onCancel, theoryBlocks }: MultipleChoiceModalProps = $props();

	let selectedBlockId = $state<string>('');
</script>

<Modal
	open={isOpen}
	contentBase="card bg-surface-100-900 p-4 space-y-4 shadow-xl max-w-3xl w-full"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<h2 class="text-lg font-bold">{$_('block.multipleChoiceCreation')}</h2>
		<div class="space-y-2">
			<label class="block">
				<span class="text-sm font-medium">{$_('block.chooseTheoryBlock')}</span>
				<select bind:value={selectedBlockId} class="select select-bordered w-full">
					<option disabled selected value="">{$_('common.choice')}</option>
					{#each theoryBlocks as block (block.id)}
						<option value={block.id}>{block.title}</option>
					{/each}
				</select>
			</label>
		</div>
		<div class="flex justify-end space-x-2">
			<button
				class="btn btn-secondary"
				onclick={() => {
					onCancel();
					selectedBlockId = '';
				}}>{$_('common.save')}</button
			>
			<button class="btn btn-primary" onclick={() => onConfirm(selectedBlockId)}
				>{$_('common.cancel')}</button
			>
		</div>
	{/snippet}
</Modal>
