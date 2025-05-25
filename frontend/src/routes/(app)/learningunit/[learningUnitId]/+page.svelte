<script lang="ts">
	import { api } from '$lib/api/apiClient.js';
	import { applyBlockActions, getLearningUnitById } from '$lib/api/collections/learningUnit.js';
	import BlockEditor from '$lib/components/blocks/BlockEditor.svelte';
	import BlockReorder from '$lib/components/blocks/BlockReorder.svelte';
	import { addBlockActionListener, blockActionState } from '$lib/states/blockActionState.svelte';
	import { toaster } from '$lib/states/toasterState.svelte';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import { _, locale } from 'svelte-i18n';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo.js';
	import { get } from 'svelte/store';

	let { data } = $props();

	blockActionState.setBlocks(data.learningUnit.blocks);
	blockActionState.clearQueue();

	let timer: ReturnType<typeof setTimeout> | null = null;
	let dataLoading = $state(false);

	const localeToLanguageValue: Record<string, string> = {
		en: 'ENGLISH',
		de: 'GERMAN',
		fr: 'FRENCH',
		it: 'ITALIAN'
	};
	let language = $state(localeToLanguageValue[get(locale) ?? 'en']);

	$effect(() => {
		const { remove } = addBlockActionListener(() => {
			if (blockActionState.queue.length === 0) {
				return;
			}

			if (timer) {
				clearTimeout(timer);
			}
			timer = setTimeout(async () => {
				const queue = blockActionState.queue; // Get the current queue
				blockActionState.clearQueue(); // Clear the queue
				try {
					const applyResult = await api(fetch)
						.req(applyBlockActions, queue, data.learningUnitId)
						.parse();
					// applyResult is a map of temporary block ids to permanent block ids, lets reflect this in our blockActionState
					blockActionState.setBlocks(
						blockActionState.blocks.map((block) => {
							const mappedId = applyResult[block.uuid];
							if (mappedId) {
								return { ...block, uuid: mappedId };
							}
							return block;
						})
					);
				} catch {
					toaster.create({
						description: $_('block.newName.danger'),
						type: 'warning'
					});
					dataLoading = true;
					const refetchedData = await api(fetch)
						.req(getLearningUnitById, null, data.learningUnitId)
						.parse();
					blockActionState.setBlocks(refetchedData.blocks);
					dataLoading = false;
				}
			}, 400);
		});

		return () => {
			remove();
		};
	});
</script>

<PageContainer
	title={$_('learningUnit.details', {
		values: { name: data.learningUnit.name }
	})}
>
	<div class="-m-4">
		<div
			class="grid h-full grid-cols-[75%_25%]"
			class:pointer-events-none={dataLoading}
			class:opacity-50={dataLoading}
			class:cursor-not-allowed={dataLoading}
		>
			<BlockEditor
				learningUnitId={data.learningUnitId}
				role={data.userInfo.role}
				onLanguageSelect={(selectedLanguage: string) => (language = selectedLanguage)}
			/>
			{#if data.userInfo.role === INSTRUCTOR_ROLE}
				<div class="sticky top-0 h-fit self-start">
					<BlockReorder {language} role={data.userInfo.role} />
				</div>
			{/if}
		</div>
	</div>
</PageContainer>
