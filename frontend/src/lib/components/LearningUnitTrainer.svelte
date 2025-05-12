<script lang="ts">
	import { blockActionState } from '$lib/states/blockActionState.svelte';
	import BlockItem from '$lib/components/blocks/BlockItem.svelte';
	import { type RoleType } from '$lib/schemas/response/UserInfo';
	import Select from './select/Select.svelte';
	import { _, locale } from 'svelte-i18n';
	import { get } from 'svelte/store';

	interface LearningUnitTrainingContainerProps {
		role: RoleType;
	}
	const { role }: LearningUnitTrainingContainerProps = $props();
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
	<div class="space-y-2">
		{#each blockActionState.blocks as block (block.uuid)}
			<div class="space-y-2">
				<BlockItem {block} {role} language={selectedLanguage} />
			</div>
		{/each}
	</div>
</div>
