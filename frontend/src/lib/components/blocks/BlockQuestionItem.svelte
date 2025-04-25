<script lang="ts">
	import { _ } from 'svelte-i18n';
	let question = $state('');
	let expectedAnswer = $state('');

</script>

<div class="rounded-lg bg-white p-4 dark:bg-gray-800">
	<input
		type="text"
		placeholder={$_('block.textAnswerBlock.question')}
		bind:value={question}
		class="input mb-4 w-full border p-2"
	/>
	<div>
		<input
			type="text"
			placeholder={$_('block.textAnswerBlock.answer')}
			bind:value={expectedAnswer}
			class="input w-full border p-2"
		/>
	</div>
</div>
