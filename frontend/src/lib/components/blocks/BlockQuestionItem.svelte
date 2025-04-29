<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import { type BlockRes, QUESTION_BLOCK_TYPE } from '$lib/schemas/response/BlockRes';
	import type { RoleType } from '$lib/schemas/response/UserInfo';

	interface BlockQuestionItemProps {
		block: Extract<BlockRes, { type: typeof QUESTION_BLOCK_TYPE }>;
		role: RoleType;
	}

	const { block }: BlockQuestionItemProps = $props();

	let question = $state(block.question);
	let expectedAnswer = $state(block.expectedAnswer);

	let lastQuestion = $state(block.question);
	let lastExpectedAnswer = $state(block.expectedAnswer);

	function handleUpdate() {
		if (question !== lastQuestion || expectedAnswer !== lastExpectedAnswer) {
			queueBlockAction({
				type: 'UPDATE_BLOCK',
				blockId: block.uuid,
				question,
				expectedAnswer
			});

			lastQuestion = question;
			lastExpectedAnswer = expectedAnswer;
		}
	}

	$effect(() => {
		handleUpdate();
	});
</script>

<div class="rounded-lg bg-white p-4 dark:bg-gray-800">
	<input
		type="text"
		placeholder={$_('common.block.question')}
		bind:value={question}
		oninput={handleUpdate}
		onblur={handleUpdate}
		class="input mb-4 w-full border p-2"
	/>
	<div>
		<input
			type="text"
			placeholder={$_('common.block.answer')}
			bind:value={expectedAnswer}
			oninput={handleUpdate}
			onblur={handleUpdate}
			class="input w-full border p-2"
		/>
	</div>
</div>
