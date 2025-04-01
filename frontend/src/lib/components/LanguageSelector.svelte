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
		class="flex items-center justify-between w-full border rounded border-surface-200-800 pl-3 pr-3 py-2 text-left focus:outline-none">
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
		<ul class="absolute z-10 mt-1 w-full bg-surface-100-900 border border-surface-200-800 rounded shadow-lg max-h-60 overflow-auto">
			{#each $locales as loc (loc)}
				<li>
					<button
						type="button"
						on:click={() => selectLocale(loc)}
						class="flex items-center justify-between w-full text-left px-3 py-2 truncate hover:bg-surface-200-800 focus:outline-none">
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
