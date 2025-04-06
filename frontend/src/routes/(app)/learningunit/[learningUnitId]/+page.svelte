<script lang="ts">
	import { isApiErrorResponse } from '$lib/api/apiError.js';
	import { browserApiClient } from '$lib/api/browserApiClient.js';
	import { applyBlockActions, getLearningUnitById } from '$lib/api/collections/learningUnit.js';
	import BlockEditor from '$lib/components/blocks/BlockEditor.svelte';
	import BlockReorder from '$lib/components/blocks/BlockReorder.svelte';
	import { addBlockActionListener, blockActionState } from '$lib/states/blockActionState.svelte';
	import { getToastContext } from '$lib/states/toastContext.svelte.js';

	const toast = getToastContext();

	let { data } = $props();

	blockActionState.setBlocks(data.learningUnit.blocks);
	blockActionState.clearQueue();

	let timer: NodeJS.Timeout | null = null;
	let dataLoading = $state(false);

	$effect(() => {
		const { remove } = addBlockActionListener(() => {
			if (blockActionState.queue.length === 0) {
				return;
			}

			timer && clearTimeout(timer);
			timer = setTimeout(async () => {
				const queue = blockActionState.queue; // Get the current queue
				blockActionState.clearQueue(); // Clear the queue
				try {
					const applyResult = await browserApiClient.req(
						applyBlockActions,
						queue,
						data.learningUnitId
					);
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
					toast.create({
						title: 'Error',
						description: `Failed to save learning unit. (${status})`,
						type: 'error'
					});
					dataLoading = true;
					const refetchedData = await browserApiClient.req(
						getLearningUnitById,
						null,
						data.learningUnitId
					);
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

<div class="-m-4">
	<div
		class="grid h-full grid-cols-[75%_25%]"
		class:pointer-events-none={dataLoading}
		class:opacity-50={dataLoading}
		class:cursor-not-allowed={dataLoading}
	>
		<BlockEditor />
		<BlockReorder />
	</div>
</div>
