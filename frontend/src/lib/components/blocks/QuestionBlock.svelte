<script lang="ts">
	let question = '';
	let answer = '';
</script>

<div>
	<input
		type="text"
		placeholder="Type Question"
		bind:value={question}
		class="input mb-4 w-full border p-2"
	/>
	<div>
		<input
			type="text"
			placeholder="{$_('')}"
			bind:value={answer}
			class="input w-full border p-2"
		/>
	</div>
</div>
