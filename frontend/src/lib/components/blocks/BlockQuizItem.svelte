<script lang="ts">
	import { _ } from 'svelte-i18n';
	let question = '';
	let answer = '';
</script>

<div class="rounded border border-green-300 bg-green-100 p-4 shadow">
	<h2 class="text-xl font-bold text-green-800">{$_('block.textAnswerQuiz')}</h2>
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
			bind:value={answer}
			class="input w-full border p-2"
		/>
	</div>
</div>
