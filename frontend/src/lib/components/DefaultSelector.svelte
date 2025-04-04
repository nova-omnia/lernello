<script lang="ts">
	import { ChevronDown, ChevronUp, Check } from 'lucide-svelte';

	export let options: string[] = [];
	export let selected: string | null = null;
	export let onSelect: (option: string) => void;

	let open = false;

	function toggleDropdown() {
		open = !open;
	}

	function selectOption(option: string) {
		onSelect(option);
		open = false;
	}
</script>

<div class="relative inline-block w-full">
	<button
		type="button"
		on:click={toggleDropdown}
		class="border-surface-200-800 flex w-full items-center justify-between rounded border py-2 pl-3 pr-3 text-left focus:outline-none"
	>
		<span class="truncate">
			{#if selected}
				<slot name="selected">{selected}</slot>
			{:else}
				Select...
			{/if}
		</span>
		<span>
			{#if open}
				<ChevronUp size={20} />
			{:else}
				<ChevronDown size={20} />
			{/if}
		</span>
	</button>
	{#if open}
		<ul
			class="bg-surface-100-900 border-surface-200-800 absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded border shadow-lg"
		>
			{#each options as option (option)}
				<li>
					<button
						type="button"
						on:click={() => selectOption(option)}
						class="hover:bg-surface-200-800 flex w-full items-center justify-between truncate px-3 py-2 text-left focus:outline-none"
					>
						<span class="truncate">
							<slot name="option" {option}></slot>
							<!-- Pass 'option' to the slot -->
						</span>
						<span class="w-5 flex-shrink-0 text-right">
							{#if option === selected}
								<Check size={16} class="text-primary-900-100" />
							{/if}
						</span>
					</button>
				</li>
			{/each}
		</ul>
	{/if}
</div>
