<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import { type BlockRes, QUESTION_BLOCK_TYPE } from '$lib/schemas/response/BlockRes';
	import type { RoleType } from '$lib/schemas/response/UserInfo';
	import { createDebounced } from '$lib/utils/createDebounced';

	interface BlockQuestionItemProps {
		block: Extract<BlockRes, { type: typeof QUESTION_BLOCK_TYPE }>;
		role: RoleType;
	}

	const { block }: BlockQuestionItemProps = $props();

	let currentQuestion = $derived(block.question);
	let currentExpectedAnswer = $derived(block.expectedAnswer);

	const onUpdateHandler = createDebounced(() => {
		if (currentQuestion !== block.question || currentExpectedAnswer !== block.expectedAnswer) {
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				question: currentQuestion,
				expectedAnswer: currentExpectedAnswer
			});
		}
	}, 500);
</script>

<div class="rounded-lg bg-white p-4 dark:bg-gray-800">
	<input
		type="text"
		placeholder={$_('common.block.question')}
		bind:value={currentQuestion}
		oninput={onUpdateHandler}
		onblur={onUpdateHandler}
		class="input mb-4 w-full border p-2"
	/>
	<div>
		<input
			type="text"
			placeholder={$_('common.block.answer')}
			bind:value={currentExpectedAnswer}
			oninput={onUpdateHandler}
			onblur={onUpdateHandler}
			class="input w-full border p-2"
		/>
	</div>
</div>
