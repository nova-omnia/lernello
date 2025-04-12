<script lang="ts">
	import { Dialog, Separator } from 'bits-ui';
	import { WandSparkles, X } from 'lucide-svelte';
	import MultiSelect from './MultiSelect.svelte';

	// Auswahl-Status
	let selectedFiles: string[] = [];

	// Beispieloptionen – kannst du natürlich dynamisch machen
	const files = [
		{ value: 'math', label: 'Mathematics' },
		{ value: 'ai', label: 'Artificial Intelligence' },
		{ value: 'ml', label: 'Machine Learning' },
		{ value: 'cv', label: 'Computer Vision' },
		{ value: 'nlp', label: 'NLP' }
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
			class="rounded-card-lg preset-filled-surface-100-900 shadow-popover data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95 outline-hidden fixed left-[50%] top-[50%] z-50 w-full max-w-[calc(100%-2rem)] translate-x-[-50%] translate-y-[-50%] border p-5 sm:max-w-[490px] md:w-full"
		>
			<Dialog.Title class="flex w-full items-center justify-center text-lg font-semibold tracking-tight">
				Creation Wizard
			</Dialog.Title>

			<Separator.Root class="bg-muted -mx-5 mb-6 mt-5 block h-px" />

			<form>
				<div class="mb-4">
					<input
						type="text"
						placeholder="Enter the Topic here..."
						class="h-input w-full rounded-input shadow-mini focus-visible:ring-dark focus-visible:ring-offset-background focus-visible:outline-hidden px-4 py-2"
					/>
				</div>

				<div class="mb-4">
					<!-- MultiSelect mit Binding -->
					<MultiSelect
						selected={selectedFiles}
						onSelect={(vals) => (selectedFiles = vals)}
						options={files}
						placeholder="Select files..."
					/>
				</div>

				<div class="flex w-full justify-end">
					<Dialog.Close
						class="h-input pt-4 rounded-input shadow-mini focus-visible:ring-dark focus-visible:ring-offset-background focus-visible:outline-hidden inline-flex justify-center font-semibold focus-visible:ring-2 focus-visible:ring-offset-2 active:scale-[0.98]"
					>
						Save
					</Dialog.Close>
				</div>
			</form>

			<Dialog.Close
				class="focus-visible:ring-foreground focus-visible:ring-offset-background focus-visible:outline-hidden absolute right-5 top-5 rounded-md focus-visible:ring-2 focus-visible:ring-offset-2 active:scale-[0.98]"
			>
				<div>
					<X class="text-foreground size-5" />
					<span class="sr-only">Close</span>
				</div>
			</Dialog.Close>
		</Dialog.Content>
	</Dialog.Portal>
</Dialog.Root>
