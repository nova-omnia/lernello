<!--frontend/src/lib/components/multiSelect.svelte-->
<script lang="ts">
	import { Combobox } from 'bits-ui';
	import { ListChecks, Check, ChevronsDown, ChevronsUp, File } from 'lucide-svelte';

	type FileItem = {
		name: string;
	};

	const files: FileItem[] = [
		{ name: 'File 1' },
		{ name: 'File 2' },
		{ name: 'File 3' },
		{ name: 'File 4' },
		{ name: 'File 5' },
		{ name: 'File 6' },
		{ name: 'File 7' },
		{ name: 'File 8' },
		{ name: 'File 9' },
		{ name: 'File 10' }
	];

	let searchValue = $state('');

	const filteredFiles = $derived(
		searchValue === ''
			? files
			: files.filter((file) => file.name.toLowerCase().includes(searchValue.toLowerCase()))
	);
</script>

<Combobox.Root
	type="multiple"
	onOpenChange={(o) => {
		if (!o) searchValue = '';
	}}
>
	<div class="relative">
		<File class="absolute start-3 top-1/2 size-6 -translate-y-1/2" />
		<Combobox.Input
			oninput={(e) => (searchValue = e.currentTarget.value)}
			class="h-input rounded-9px border-border-input bg-surface-100-900 placeholder:text-foreground-alt/50 inline-flex w-full truncate border px-11 text-base transition-colors focus:outline-none focus:ring-0 sm:text-sm"
			placeholder="Search a file"
			aria-label="Search a file"
		/>
		<Combobox.Trigger class="absolute end-3 top-1/2 size-6 -translate-y-1/2">
			<ListChecks class="text-muted-foreground size-6" />
		</Combobox.Trigger>
	</div>
	<Combobox.Portal>
		<Combobox.Content
			class="focus-override border-muted bg-surface-100-900 shadow-popover outline-hidden z-50 max-h-55 w-[var(--bits-combobox-anchor-width)] min-w-[var(--bits-combobox-anchor-width)] select-none overflow-auto rounded-xl border px-1 py-3"
			style="height: auto;"
			sideOffset={10}
		>
			<Combobox.ScrollUpButton class="flex w-full items-center justify-center py-1">
				<ChevronsUp class="size-3" />
			</Combobox.ScrollUpButton>
			<Combobox.Viewport class="p-1">
				{#each filteredFiles as file, i (i + file.name)}
					<Combobox.Item
						class="rounded-button data-highlighted:bg-muted outline-hidden flex h-10 w-full select-none items-center py-3 pl-5 pr-1.5 text-sm capitalize"
						value={file.name}
					>
						{#snippet children({ selected })}
							{file.name}
							{#if selected}
								<div class="ml-auto">
									<Check />
								</div>
							{/if}
						{/snippet}
					</Combobox.Item>
				{:else}
					<span class="block px-5 py-2 text-sm text-muted-foreground"> No results found </span>
				{/each}
			</Combobox.Viewport>
			<Combobox.ScrollDownButton class="flex w-full items-center justify-center py-1">
				<ChevronsDown class="size-3" />
			</Combobox.ScrollDownButton>
		</Combobox.Content>
	</Combobox.Portal>
</Combobox.Root>
