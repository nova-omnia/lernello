<!--AITheoryBlock.svelte-->
<script lang="ts">
	import { Dialog, Separator } from 'bits-ui';
	import { WandSparkles, X } from 'lucide-svelte';
	import MultiSelect from './MultiSelect.svelte';
	import { _ } from 'svelte-i18n';

	interface Option {
		uuid: string;
		name: string;
	}

	let selectedFiles: Option[] = [];

	const files: Option[] = [
		{ uuid: '550e8400-e29b-41d4-a716-446655440000', name: 'Mathematics' },
		{ uuid: '550e8400-e29b-41d4-a716-446655440001', name: 'Artificial Intelligence' },
		{ uuid: '550e8400-e29b-41d4-a716-446655440002', name: 'Machine Learning' },
		{ uuid: '550e8400-e29b-41d4-a716-446655440003', name: 'Computer Vision' },
		{ uuid: '550e8400-e29b-41d4-a716-446655440004', name: 'NLP' }
	];
</script>

<Dialog.Root>
	<Dialog.Trigger>
		<WandSparkles />
	</Dialog.Trigger>

	<Dialog.Portal>
		<Dialog.Overlay
			class="data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 fixed inset-0 z-50 bg-black/80"
		/>
		<Dialog.Content
			class="rounded-card-lg preset-filled-surface-100-900 shadow-popover data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95 fixed top-[50%] left-[50%] z-50 w-full max-w-[calc(100%-2rem)] translate-x-[-50%] translate-y-[-50%] border p-5 outline-hidden sm:max-w-[490px] md:w-full"
		>
			<Dialog.Title
				class="flex w-full items-center justify-center text-lg font-semibold tracking-tight"
			>
				{$_('dialog.creationWizardTitle')}
			</Dialog.Title>
			<Separator.Root class="bg-muted -mx-5 mt-5 mb-6 block h-px" />
			<form>
				<div class="text-primary-600-400 mb-4">
					<input
						type="text"
						placeholder={$_('dialog.enterTopicPlaceholder')}
						class="h-input rounded-input shadow-mini focus-visible:ring-dark focus-visible:ring-offset-background w-full px-4 py-2 focus-visible:outline-hidden"
					/>
				</div>

				<div class="text-primary-600-400 mb-4">
					<MultiSelect
						selected={selectedFiles}
						onSelect={(vals) => (selectedFiles = vals)}
						options={files}
						placeholder={$_('dialog.selectFilesPlaceholder')}
					/>
				</div>

				<div class="flex w-full justify-end">
					<Dialog.Close
						class="h-input rounded-input shadow-mini focus-visible:ring-dark focus-visible:ring-offset-background inline-flex justify-center pt-4 font-semibold focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:outline-hidden active:scale-[0.98]"
					>
						{$_('dialog.saveButton')}
					</Dialog.Close>
				</div>
			</form>
			<Dialog.Close
				class="focus-visible:ring-foreground focus-visible:ring-offset-background absolute top-5 right-5 rounded-md focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:outline-hidden active:scale-[0.98]"
			>
				<div>
					<X class="text-foreground size-5" />
					<span class="sr-only">{$_('dialog.closeButton')}</span>
				</div>
			</Dialog.Close>
		</Dialog.Content>
	</Dialog.Portal>
</Dialog.Root>
