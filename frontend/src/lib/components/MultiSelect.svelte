<script lang="ts">
	import { onMount } from 'svelte';
	import { _ } from 'svelte-i18n';

	interface Option {
		uuid: string;
		label: string;
	}

	interface MultiSelectProps {
		selected?: Option[];
		onSelect: (selected: Option[]) => void;
		options: Option[];
		placeholder?: string;
	}

	const { selected = [], onSelect, options, placeholder }: MultiSelectProps = $props();

	let open = $state(false);
	let searchValue = $state('');
	let container: HTMLElement;
	let inputRef: HTMLInputElement | null = null;

	const placeHolder = $derived(() => placeholder ?? $_('multiSelect.placeholder'));

	const filteredOptions = $derived(() =>
		searchValue
			? options.filter((o) => o.label.toLowerCase().includes(searchValue.toLowerCase()))
			: options
	);

	const selectedSet = $derived(() => new Set(selected.map((s) => s.uuid)));
	const isSelected = (uuid: string) => selectedSet().has(uuid);

	const selectOption = (uuid: string) => {
		const found = options.find((opt) => opt.uuid === uuid);
		if (!found) return;

		onSelect(isSelected(uuid) ? selected.filter((o) => o.uuid !== uuid) : [...selected, found]);

		searchValue = '';
	};

	function handleClickOutside(e: MouseEvent) {
		if (!container.contains(e.target as Node)) {
			open = false;
			searchValue = '';
		}
	}

	function handleKeydown(e: KeyboardEvent) {
		if (e.key === 'Escape') {
			open = false;
			searchValue = '';
			inputRef?.blur();
		}
	}

	onMount(() => {
		window.addEventListener('click', handleClickOutside);
		window.addEventListener('keydown', handleKeydown);
		return () => {
			window.removeEventListener('click', handleClickOutside);
			window.removeEventListener('keydown', handleKeydown);
		};
	});
</script>

<div class="relative inline-block w-full" bind:this={container}>
	<input
		type="text"
		placeholder={placeHolder()}
		bind:value={searchValue}
		bind:this={inputRef}
		onfocus={() => (open = true)}
		class="bg-surface-100-900 w-full rounded border px-3 py-2 outline-none focus:border-transparent focus:shadow-none focus:ring-0 focus:outline-none"
		aria-label="Search or select"
	/>

	{#if open}
		<div class="bg-surface-100-900 absolute z-10 mt-1 w-full rounded border shadow-lg">
			<ul class="max-h-60 space-y-1 overflow-auto px-1 py-1">
				{#each filteredOptions() as option (option.uuid)}
					<li>
						<button
							type="button"
							onclick={() => selectOption(option.uuid)}
							class={`hover:bg-surface-200-800 flex w-full items-center justify-between px-3 py-2 text-left focus:outline-none ${
								isSelected(option.uuid) ? 'bg-surface-200-800' : ''
							}`}
						>
							<div class="grid w-full grid-cols-[300px_auto] items-center">
								<p>{option.label.split('|')[0]}</p>
								<p>{option.label.split('|')[1]}</p>
							</div>
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
