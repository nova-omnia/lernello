<script lang="ts">
	import { _, locale, locales } from 'svelte-i18n';
	import { Check, ChevronDown, ChevronUp, Globe } from 'lucide-svelte';

	let open = false;

	function toggleDropdown() {
		open = !open;
	}

	function selectLocale(loc: string) {
		locale.set(loc);
		open = false;
	}
</script>

<div class="relative inline-block w-40">
	<button
		type="button"
		on:click={toggleDropdown}
		class="border-surface-200-800 flex w-full items-center justify-between rounded border py-2 pr-3 pl-3 text-left focus:outline-none"
	>
		<span class="flex items-center space-x-2">
			<Globe size={20} />
			<span class="truncate">
				{#if $locale}
					{$_(`languageName.${$locale}`)}
				{:else}
					Select...
				{/if}
			</span>
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
			{#each $locales as loc (loc)}
				<li>
					<button
						type="button"
						on:click={() => selectLocale(loc)}
						class="hover:bg-surface-200-800 flex w-full items-center justify-between truncate px-3 py-2 text-left focus:outline-none"
					>
						<span class="truncate">
							{$_(`languageName.${loc}`)}
						</span>
						<span class="w-5 flex-shrink-0 text-right">
							{#if loc === $locale}
								<Check size={16} class="text-primary-50" />
							{/if}
						</span>
					</button>
				</li>
			{/each}
		</ul>
	{/if}
</div>
