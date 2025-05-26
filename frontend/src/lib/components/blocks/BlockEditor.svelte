<script lang="ts">
	import { blockActionState } from '$lib/states/blockActionState.svelte';
	import BlockItem from '$lib/components/blocks/BlockItem.svelte';
	import { flip } from 'svelte/animate';
	import BlockSelectPopover from './BlockSelectPopover.svelte';
	import type { RoleType } from '$lib/schemas/response/UserInfo';
	import { _, locale } from 'svelte-i18n';
	import Select from '../select/Select.svelte';
	import { get } from 'svelte/store';

	interface BlockEditorProps {
		learningUnitId: string;
		role: RoleType;
		onLanguageSelect: (language: string) => string;
	}

	const { learningUnitId, role, onLanguageSelect }: BlockEditorProps = $props();

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

<div class="preset-filled-surface-50-950 border-surface-100-900 border-r pr-4">
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
				onLanguageSelect(selectedLanguage);
			}}
		/>
	</div>

	<BlockSelectPopover index={-1} {learningUnitId} language={selectedLanguage}/>
	{#each blockActionState.blocks as block, index (block.uuid)}
		<div id={`block-${block.uuid}`} animate:flip={{ duration: 200 }} class="space-y-2">
			<BlockItem {block} {role} language={selectedLanguage} />
			<BlockSelectPopover {index} {learningUnitId} language={selectedLanguage}/>
		</div>
	{/each}
</div>
