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
	contentBase="card bg-surface-100-900 w-full p-8 space-y-4 shadow-xl max-w-xl border-surface-500"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<h1 class="preset-typo-headline">{$_('block.multipleChoiceCreation')}</h1>

		<div class="space-y-4">
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

		<footer class="flex justify-end gap-3 pt-2">
			<button
				type="button"
				class="btn preset-tonal-surface"
				onclick={() => {
					onCancel();
					selectedBlockId = '';
				}}
			>
				{$_('common.cancel')}
			</button>
			<button
				type="button"
				class="btn preset-filled-primary-500"
				onclick={() => onConfirm(selectedBlockId)}
			>
				{$_('common.save')}
			</button>
		</footer>
	{/snippet}
</Modal>
