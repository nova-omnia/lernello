<!-- MultiSelect.svelte -->
<script lang="ts">
    import { writable, derived } from 'svelte/store';
    import { Check, ChevronDown, ChevronUp, X } from 'lucide-svelte';
    import { _ } from 'svelte-i18n';

    export interface Option {
        uuid: string;
        label: string;
    }

    export interface MultiSelectProps {
        selected?: Option[];
        onSelect: (selected: Option[]) => void;
        options: Option[];
        placeholder?: string;
    }

    let { selected = [], onSelect, options, placeholder = $_('multiSelect.placeholder') }: MultiSelectProps = $props();

    let open = writable(false);
    let searchValue = writable('');

    const toggleDropdown = () => open.update((val) => !val);

    const isSelected = (uuid: string) => selected.some((opt) => opt.uuid === uuid);

    const selectOption = (uuid: string) => {
        const found = options.find((opt) => opt.uuid === uuid);
        if (!found) return;
        onSelect(
            isSelected(uuid)
                ? selected.filter((o) => o.uuid !== uuid)
                : [...selected, found]
        );
    };

    const removeSelection = (uuid: string) => onSelect(selected.filter((o) => o.uuid !== uuid));

    const clearAll = () => onSelect([]);

    const filteredOptions = derived(searchValue, ($searchValue) => {
        return $searchValue
            ? options.filter((o) => o.label.toLowerCase().includes($searchValue.toLowerCase()))
            : options;
    });
</script>

<div class="relative inline-block w-full">
    <button type="button" onclick={toggleDropdown} class="border-surface-200-800 flex w-full flex-wrap items-center justify-between gap-2 rounded border py-2 pl-3 pr-3 text-left focus:outline-none">
        <div class="flex max-w-[80%] flex-wrap gap-1">
            {#if selected.length > 0}
                {#each selected as option (option.uuid)}
                    <span class="bg-primary-100-900 flex items-center gap-1 rounded px-2 py-0.5 text-sm">
                        {option.label}
                        <X class="size-3 cursor-pointer" on:click={() => removeSelection(option.uuid)} />
                    </span>
                {/each}
            {:else}
                <span>{placeholder}</span>
            {/if}
        </div>
        <span class="flex items-center gap-2">
            {#if selected.length > 0}
                <X size={16} onclick={clearAll} class="text-muted-foreground cursor-pointer" />
            {/if}
            {#if $open}
                <ChevronUp size={20} />
            {:else}
                <ChevronDown size={20} />
            {/if}
        </span>
    </button>

    {#if $open}
        <div class="bg-surface-100-900 absolute z-10 mt-1 w-full rounded border shadow-lg">
            <input type="text" placeholder={$_('multiSelect.searchPlaceholder')} bind:value={$searchValue} class="w-full border-b px-3 py-2 outline-none" />
            <ul class="max-h-60 overflow-auto">
                {#each $filteredOptions as option (option.uuid)}
                    <li>
                        <button type="button" onclick={() => selectOption(option.uuid)} class="hover:bg-surface-200-800 flex w-full items-center justify-between px-3 py-2 text-left focus:outline-none">
                            <span>{option.label}</span>
                            {#if isSelected(option.uuid)}
                                <Check size={16} class="text-primary-900-100" />
                            {/if}
                        </button>
                    </li>
                {:else}
                    <li class="px-3 py-2 text-muted-foreground text-sm">{$_('multiSelect.noOptionsFound')}</li>
                {/each}
            </ul>
        </div>
    {/if}
</div>