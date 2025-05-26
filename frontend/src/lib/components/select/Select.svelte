<!--frontend/src/lib/components/Select.svelte-->
<script lang="ts">
	import { Check, ChevronDown, ChevronUp } from 'lucide-svelte';
	import { onMount } from 'svelte';

	interface SelectProps {
		selected?: string | null;
		onSelect: (option: string) => void;
		options: { value: string; label: string }[];
	}

	let { selected, onSelect, options }: SelectProps = $props();
	let open = $state(false);
	let container: HTMLElement;

	function selectOption(option: string) {
		onSelect(option);
		open = false;
	}

	function handleClickOutside(e: MouseEvent) {
		if (!container.contains(e.target as Node)) {
			open = false;
		}
	}

	function handleKeydown(e: KeyboardEvent) {
		if (e.key === 'Escape') {
			open = false;
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

{#snippet figure(item: string)}
	<p>{item}</p>
{/snippet}

<div class="relative inline-block w-full" bind:this={container}>
	<button
		type="button"
		onclick={() => (open = !open)}
		class="preset-filled-surface-50-950 border-surface-200-800 flex h-10 w-full items-center justify-between rounded-md border py-2 pr-3 pl-3 text-left focus:outline-none"
	>
		<span class="truncate">
			{#if selected}
				{@render figure(
					options.find((option) => selected.includes(option.value))?.label || selected
				)}
			{:else}
				Select...
			{/if}
		</span>
		<span>
			{#if open}
				<ChevronUp size={20} style="pointer-events: none;" />
			{:else}
				<ChevronDown size={20} style="pointer-events: none;" />
			{/if}
		</span>
	</button>
	{#if open}
		<ul
			class="preset-filled-surface-50-950 border-surface-200-800 absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded border shadow-lg"
		>
			{#each options as option (option)}
				<li>
					<button
						type="button"
						onclick={() => selectOption(option.value)}
						class="hover:bg-surface-100-900 flex w-full items-center justify-between truncate px-3 py-2 text-left focus:outline-none"
					>
						<span class="truncate">
							{@render figure(option.label)}
						</span>
						<span class="w-5 flex-shrink-0 text-right">
							{#if option.value === selected}
								<Check size={16} class="text-primary-900-100" />
							{/if}
						</span>
					</button>
				</li>
			{/each}
		</ul>
	{/if}
</div>
