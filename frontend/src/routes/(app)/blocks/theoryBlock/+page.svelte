<script lang="ts">
	import { MarkdownEditor } from 'carta-md';
	import { carta } from '$lib/carta';
	export let data: { markdown: string; html: string };
</script>

<div class="flex min-h-screen w-full items-center justify-center">
	<div class="mx-auto h-[600px] w-[1000px] rounded border p-4">
		<MarkdownEditor bind:value={data.markdown} mode="tabs" {carta} />
	</div>
</div>

<form method="post" action="?/create" class="flex flex-col space-y-4">
    <input type="text" name="name" placeholder="Block Name" required class="border p-2" />
    <input type="number" name="position" placeholder="Position" required class="border p-2" />
    <input type="text" name="learningUnitId" placeholder="Learning Unit ID" required class="border p-2" />
    <textarea name="content" placeholder="Content" required class="border p-2"></textarea>
    <button type="submit" class="bg-blue-500 text-white p-2 rounded">Create TheoryBlock</button>
</form>

<style>
	:global(.carta-toolbar) {
		padding-bottom: 1rem;
	}
	:global(.carta-toolbar-left button) {
		margin-right: 1rem;
	}
	:global(.carta-toolbar-right button) {
		margin-left: 0.5rem;
	}

	:global(.carta-editor) {
		width: 100%;
		height: 100%;
	}

	:global(.carta-renderer) {
		width: 100%;
		height: 100%;
		background-color: black;
	}

	:global(.carta-font-code) {
		width: 100%;
		height: 100%;
		background-color: white;
	}

	:global(-carta-input-wrapper) {
		width: 100%;
		height: 100%;
		background-color: red;
	}

	:global(.carta-input) {
		display: flex;
		flex-direction: column;
		width: 100%;
		height: 100%;
	}

	:global(.carta-input textarea) {
		flex: 1;
		width: 100%;
		height: 100%;
		resize: none; /* Optional: Prevent resizing if not YY Y needed */
	}
</style>
