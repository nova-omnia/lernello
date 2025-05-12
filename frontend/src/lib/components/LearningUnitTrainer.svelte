<script lang="ts">
	import { blockActionState } from '$lib/states/blockActionState.svelte.js';
	import BlockItem from '$lib/components/blocks/BlockItem.svelte';
	import type { RoleType } from '$lib/schemas/response/UserInfo.js';
	import BlockOverviewItem from '$lib/components/blocks/BlockOverviewItem.svelte';
	import Select from './select/Select.svelte';
	import { _, locale } from 'svelte-i18n';
	import { get } from 'svelte/store';

	interface LearningUnitTrainerProps {
		role: RoleType;
	}

	const { role }: LearningUnitTrainerProps = $props();

	function scrollToBlock(blockId: string) {
		const element = document.getElementById(`block-${blockId}`);
		if (element) {
			element.scrollIntoView({ behavior: 'smooth', block: 'start' });
		}
	}
	const languageOptions = $derived([
		{ value: 'ENGLISH', label: $_('common.english') },
		{ value: 'GERMAN', label: $_('common.german') },
		{ value: 'FRENCH', label: $_('common.french') },
		{ value: 'ITALIAN', label: $_('common.italian') }
	]);

	const localeToLanguageValue: Record<string, string> = {
		en: 'ENGLISH',
		de: 'GERMAN',
		fr: 'FRENCH',
		it: 'ITALIAN',
		'de-CH': 'GERMAN'
	};

	let selectedLanguage: string = $state(localeToLanguageValue[get(locale) ?? 'en']);
</script>

<div class="grid h-full grid-cols-[75%_25%]">
	<div
		class="preset-filled-surface-50-950 border-surface-100-900 m-0 space-y-4 overflow-y-auto border-r p-4"
	>
		<div
			class="min-w-[120px]"
			role="presentation"
			tabindex="-1"
			onclick={(event) => {
				event.preventDefault();
				event.stopPropagation();
			}}
		>
			<Select
				options={languageOptions}
				selected={selectedLanguage}
				onSelect={(value) => {
					selectedLanguage = value;
				}}
			/>
		</div>
		{#if blockActionState.blocks.length === 0}
			<p>{$_('learningUnit.noBlocks')}</p>
		{:else}
			{#each blockActionState.blocks as block (block.uuid)}
				<div id={`block-${block.uuid}`} class="block-wrapper">
					<BlockItem {block} {role} language={selectedLanguage} />
				</div>
			{/each}
		{/if}
	</div>
	{#if blockActionState.blocks.length > 0}
		<div class="preset-filled-surface-50-950 space-y-4 overflow-y-auto p-4">
			{#each blockActionState.blocks as block (block.uuid)}
				<BlockOverviewItem {block} {role} scrollToBlock={() => scrollToBlock(block.uuid)} language={selectedLanguage}/>
			{/each}
		</div>
	{/if}
</div>
