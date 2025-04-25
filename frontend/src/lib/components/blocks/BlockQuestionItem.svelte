<script lang="ts">
	import { _ } from 'svelte-i18n';

	const { block } = $props();

	let question = block.question;
	let expectedAnswer = block.expectedAnswer;
</script>

<div class="rounded-lg bg-white p-4 dark:bg-gray-800">
	<input
		type="text"
		placeholder={$_('common.block.question')}
		bind:value={question}
		class="input mb-4 w-full border p-2"
	/>
	<div>
		<input
			type="text"
			placeholder={$_('common.block.answer')}
			bind:value={expectedAnswer}
			class="input w-full border p-2"
		/>
	</div>
</div>
