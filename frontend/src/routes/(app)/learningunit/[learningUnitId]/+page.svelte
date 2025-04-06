<script lang="ts">
	import { isApiErrorResponse } from '$lib/api/apiError.js';
	import { browserApiClient } from '$lib/api/browserApiClient.js';
	import { applyBlockActions } from '$lib/api/collections/learningUnit.js';
	import BlockEditor from '$lib/components/blocks/BlockEditor.svelte';
	import BlockReorder from '$lib/components/blocks/BlockReorder.svelte';
	import { addBlockActionListener, blockActionState } from '$lib/states/blockActionState.svelte';
	import { getToastContext } from '$lib/states/toastContext.svelte.js';

	const toast = getToastContext();

	let { data } = $props();

	blockActionState.blocks = data.blocks;
	blockActionState.queue = [];

	let timer: NodeJS.Timeout | null = null;

	$effect(() => {
		const { remove } = addBlockActionListener(() => {
			if (blockActionState.queue.length === 0) {
				return;
			}

			timer && clearTimeout(timer);
			timer = setTimeout(async () => {
				const queue = blockActionState.queue; // Get the current queue
				blockActionState.queue = []; // Clear the queue
				try {
					await browserApiClient.req(applyBlockActions, queue, data.learningUnitId);
				} catch (error) {
					const status = isApiErrorResponse(error) ? error.status : 'save';
					toast.create({
						title: 'Error',
						description: `Failed to save learning unit. (${status})`,
						type: 'error'
					});
				}
			}, 1000);
		});

		return () => {
			remove();
		};
	});
</script>

<div class="-m-4">
	<div class="grid h-full grid-cols-[75%_25%]">
		<BlockEditor />
		<BlockReorder />
	</div>
</div>
