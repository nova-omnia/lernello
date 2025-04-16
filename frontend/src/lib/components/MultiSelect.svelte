<!-- MultiSelect.svelte -->
<script lang="ts">
	import { Check, ChevronDown, ChevronUp, X } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';


	interface MultiSelectProps {
		selected?: Option[];
		onSelect: (selected: Option[]) => void;
		options?: Option[];
		placeholder?: string;
	}

	const {
		selected = [],
		onSelect,
		options,
		placeholder = $_('multiSelect.placeholder')
	}: MultiSelectProps = $props();

	let open = $state(false);
	let searchValue = $state('');
	let filteredOptions = $state(options); // Initialize with all options

	$effect(() => {
		const search = searchValue.toLowerCase();
		filteredOptions = searchValue
			? options.filter((o) =>
				[o.username, o.name, o.surname].some((field) =>
					field.toLowerCase().includes(search)
				)
			)
			: options;
	});

	const isSelected = (uuid: string) => selected.some((opt) => opt.uuid === uuid);

	const selectOption = (uuid: string) => {
		const found = options.find((opt) => opt.uuid === uuid);
		if (!found) return;
		onSelect(isSelected(uuid) ? selected.filter((o) => o.uuid !== uuid) : [...selected, found]);
	};
</script>

<div class="relative inline-block w-full">
	<button
		type="button"
		onclick={() => (open = !open)}
		class="border-surface-200-800 flex w-full flex-wrap items-center justify-between gap-2 rounded border py-2 pl-3 pr-3 text-left focus:outline-none"
	>
		<div class="flex-wrap gap-1">
			<p>{placeholder}</p>
		</div>
		{#if open}
			<ChevronUp size={20} />
		{:else}
			<ChevronDown size={20} />
		{/if}
	</button>

	{#if open}
		<div class="bg-surface-100-900 absolute z-10 mt-1 w-full rounded border shadow-lg">
			<input
				type="text"
				placeholder={$_('multiSelect.searchPlaceholder')}
				bind:value={searchValue}
				class="w-full border-b px-3 py-2 outline-none"
			/>
			<ul class="max-h-60 overflow-auto">
				{#each filteredOptions as option (option.uuid)}
					<li>
						<button
							type="button"
							onclick={() => selectOption(option.uuid)}
							class="hover:bg-surface-200-800 flex w-full items-center justify-between px-3 py-2 text-left focus:outline-none"
						>
							<span>{option.name}</span>
							{#if isSelected(option.uuid)}
								<Check size={16} class="text-primary-900-100" />
							{/if}
						</button>
					</li>
				{:else}
					<li class="px-3 py-2 text-muted-foreground text-sm">
						{$_('multiSelect.noOptionsFound')}
					</li>
				{/each}
			</ul>
		</div>
	{/if}
</div>
