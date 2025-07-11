<script lang="ts">
	import { onMount } from 'svelte';
	import { _ } from 'svelte-i18n';
	import { ChevronDown, ChevronUp, Check } from 'lucide-svelte';

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
			? options.filter((option) => option.label.toLowerCase().includes(searchValue.toLowerCase()))
			: options
	);

	const selectedSet = $derived(() => new Set(selected.map((s) => s.uuid)));
	const isSelected = (uuid: string) => selectedSet().has(uuid);

	const selectOption = (uuid: string) => {
		const found = options.find((option) => option.uuid === uuid);
		if (!found) return;

		onSelect(
			isSelected(uuid) ? selected.filter((option) => option.uuid !== uuid) : [...selected, found]
		);

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

	function handleToggle() {
		if (document.activeElement === inputRef && open) {
			open = false;
			inputRef?.blur();
			searchValue = '';
		} else if (!open) {
			open = true;
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
	<div class="relative w-full">
		<input
			type="text"
			placeholder={placeHolder()}
			bind:value={searchValue}
			bind:this={inputRef}
			onclick={() => handleToggle()}
			class="input card preset-filled-surface-50-950 border-surface-300-700 w-fill py-3 pr-10"
		/>
		<span class="absolute top-1/2 right-3 -translate-y-1/2" style="pointer-events: none;">
			{#if open}
				<ChevronUp size={20} />
			{:else}
				<ChevronDown size={20} />
			{/if}
		</span>
	</div>

	{#if open}
		<div class="card bg-surface-50-950 border-surface-500 absolute z-10 mt-1 w-full border">
			<ul class="max-h-60 space-y-1 overflow-auto p-1">
				{#each filteredOptions() as option (option.uuid)}
					<li>
						<button
							type="button"
							onclick={() => selectOption(option.uuid)}
							class="card hover:preset-filled-primary-50-950 flex w-full items-center justify-between p-3 text-left"
							class:preset-filled-surface-100-900={isSelected(option.uuid)}
						>
							<div class="grid w-full grid-cols-[1fr_auto] items-center gap-4">
								<div class="truncate">
									<p>{option.label.split('|')[0]}</p>
									<p class="text-muted-foreground text-xs">{option.label.split('|')[1]}</p>
								</div>
								{#if isSelected(option.uuid)}
									<Check size={16} class="text-primary-900-100 flex-shrink-0" />
								{/if}
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
