<script lang="ts">
	import { api } from '$lib/api/apiClient.js';
	import { applyBlockActions, getLearningUnitById } from '$lib/api/collections/learningUnit.js';
	import BlockEditor from '$lib/components/blocks/BlockEditor.svelte';
	import BlockReorder from '$lib/components/blocks/BlockReorder.svelte';
	import {
		addBlockActionListener,
		blockActionState,
		isBlockSaving
	} from '$lib/states/blockActionState.svelte';
	import { toaster } from '$lib/states/toasterState.svelte';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import { _, locale } from 'svelte-i18n';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo.js';
	import { get } from 'svelte/store';
	import ToggleViewButton from '$lib/components/ToggleViewButton.svelte';

	let { data } = $props();

	blockActionState.setBlocks(data.learningUnit.blocks);
	blockActionState.clearQueue();

	let dataLoading = $state(false);

	const localeToLanguageValue: Record<string, string> = {
		en: 'ENGLISH',
		de: 'GERMAN',
		fr: 'FRENCH',
		it: 'ITALIAN'
	};
	let language = $state(localeToLanguageValue[get(locale) ?? 'en']);

	const taskQueue: (() => Promise<void>)[] = [];
	let isRunning = false;
	let onAllTasksComplete: (() => void) | null = null;

	function enqueueTask(task: () => Promise<void>, onComplete?: () => void) {
		taskQueue.push(task);
		if (onComplete) onAllTasksComplete = onComplete;
		if (!isRunning) runNext();
	}

	async function runNext() {
		if (taskQueue.length === 0) {
			isRunning = false;
			if (onAllTasksComplete) {
				onAllTasksComplete();
				onAllTasksComplete = null;
			}
			return;
		}
		isRunning = true;
		const task = taskQueue.shift();
		if (task) {
			await task();
		}
		runNext();
	}

	$effect(() => {
		const { remove } = addBlockActionListener(() => {
			if (blockActionState.queue.length === 0) return;

			const queueToApply = [...blockActionState.queue];
			blockActionState.clearQueue();

			enqueueTask(
				async () => {
					isBlockSaving.set(true);
					await api(fetch).req(applyBlockActions, queueToApply, data.learningUnitId).parse();
				},
				async () => {
					try {
						await new Promise((resolve) => setTimeout(resolve, 1000));
						const refetched = await api(fetch)
							.req(getLearningUnitById, null, data.learningUnitId)
							.parse();
						blockActionState.setBlocks(refetched.blocks);
					} catch (err) {
						console.error(err);
						toaster.create({
							description: $_('block.newName.danger'),
							type: 'warning'
						});
					} finally {
						isBlockSaving.set(false);
						dataLoading = false;
					}
				}
			);
		});

		return () => remove();
	});
</script>

<PageContainer
	title={$_('learningUnit.details', {
		values: { name: data.learningUnit.name }
	})}
>
	<ToggleViewButton />
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
			<div class="sticky top-1 h-fit self-start">
				<BlockReorder {language} role={data.userInfo.role} />
			</div>
		{/if}
	</div>
</PageContainer>
