<script lang="ts">
	import { api } from '$lib/api/apiClient.js';
	import { isApiErrorResponse } from '$lib/api/apiError.js';
	import { applyBlockActions, getLearningUnitById } from '$lib/api/collections/learningUnit.js';
	import BlockEditor from '$lib/components/blocks/BlockEditor.svelte';
	import BlockReorder from '$lib/components/blocks/BlockReorder.svelte';
	import { addBlockActionListener, blockActionState } from '$lib/states/blockActionState.svelte';
	import { toaster } from '$lib/states/toasterState.svelte';
	import PageContainer from '$lib/components/PageContainer.svelte';
	import { _ } from 'svelte-i18n';


	let { data } = $props();

	blockActionState.setBlocks(data.learningUnit.blocks);
	blockActionState.clearQueue();

	let timer: ReturnType<typeof setTimeout> | null = null;
	let dataLoading = $state(false);

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
				} catch (error) {
					const status = isApiErrorResponse(error) ? error.status : 'save';
					toaster.create({
						title: 'Error',
						description: `Failed to save learning unit. (${status})`,
						type: 'error'
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

<PageContainer title={$_('learningUnit.details')}>
	<div class="-m-4">
		<div
			class="grid h-full grid-cols-[75%_25%]"
			class:pointer-events-none={dataLoading}
			class:opacity-50={dataLoading}
			class:cursor-not-allowed={dataLoading}
		>
			<BlockEditor learningUnitId={data.learningUnitId} />
			<BlockReorder />
		</div>
	</div>
</PageContainer>
