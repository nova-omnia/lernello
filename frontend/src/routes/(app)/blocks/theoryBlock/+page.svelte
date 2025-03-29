<script lang="ts">
	import { redirect } from '@sveltejs/kit';
	import { MarkdownEditor } from 'carta-md';
	import { carta } from '$lib/carta';
	export let data: { markdown: string; html: string };
</script>

<div class="flex min-h-screen w-full items-center justify-center">
	<div class="mx-auto h-[600px] w-[1000px] rounded border p-4">
		<MarkdownEditor bind:value={data.markdown} mode="tabs" {carta} />
	</div>
</div>

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
