<script lang="ts">
	import TextEditor from '$lib/components/MarkdownEditor/TextEditor.svelte';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import { THEORY_BLOCK_TYPE } from '$lib/schemas/response/BlockRes';
	import { type RoleType, TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';
	import { createDebounced } from '$lib/utils/createDebounced';
	import { browser } from '$app/environment';
	import { api } from '$lib/api/apiClient.js';
	import { markTheoryBlockViewed } from '$lib/api/collections/progress.js';
	import { toaster } from '$lib/states/toasterState.svelte.js';
	import { _ } from 'svelte-i18n';
	import { onMount } from 'svelte';

	interface BlockTheoryItemProps {
		block: Extract<BlockRes, { type: typeof THEORY_BLOCK_TYPE }>;
		role: RoleType;
		language: string;
	}

	const { block, role, language }: BlockTheoryItemProps = $props();
	let lastContent = $derived(
		block.translatedContents.find((content) => content.language == language)?.content ??
			block.content
	);
	let blockId: string = $derived(
		block.translatedContents.find((content) => content.language == language)?.id ?? block.uuid
	);
	let element: HTMLDivElement | undefined = $state();
	let viewed = $state(false); // Prevent multiple API calls
	let viewTimeoutId: ReturnType<typeof setTimeout> | undefined = undefined;

	const onUpdateHandler = createDebounced((newContent: string) => {
		if (newContent !== lastContent) {
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId,
				content: newContent
			});
			lastContent = newContent;
		}
	}, 500);

	onMount(() => {
		if (!browser || role !== TRAINEE_ROLE || !element) return;

		const observer = new IntersectionObserver(
			(entries) => {
				entries.forEach((entry) => {
					if (entry.isIntersecting && !viewed) {
						if (viewTimeoutId) {
							clearTimeout(viewTimeoutId);
						}
						viewTimeoutId = setTimeout(() => {
							if (viewed) return;

							viewed = true;
							const fetchFn = typeof window !== 'undefined' ? window.fetch : undefined;
							if (!fetchFn) return;

							api(fetchFn)
								.req(markTheoryBlockViewed, { blockId })
								.parse()
								.catch((err) => {
									console.error(`Failed to mark theory block ${blockId} as viewed:`, err);
									toaster.create({
										title: $_('common.error.title'),
										description: $_('error.description', { values: { status: 'unknown' } }),
										type: 'error'
									});
									viewed = false;
								});
							observer.unobserve(entry.target);
						}, 3000);
					} else {
						if (viewTimeoutId) {
							clearTimeout(viewTimeoutId);
							viewTimeoutId = undefined;
						}
					}
				});
			},
			{ threshold: 0.5 }
		);

		observer.observe(element);

		return () => {
			if (viewTimeoutId) {
				clearTimeout(viewTimeoutId);
			}
			observer.disconnect();
		};
	});
</script>

<div bind:this={element}>
	<TextEditor
		content={block.translatedContents.find((content) => content.language == language)?.content ??
			block.content}
		onUpdate={onUpdateHandler}
		{role}
	/>
</div>
