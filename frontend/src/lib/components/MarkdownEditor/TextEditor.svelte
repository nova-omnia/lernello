<script lang="ts">
	import { marked } from 'marked';
	import DOMPurify from 'dompurify';
	import Toolbar from './Toolbar.svelte';
	import { _ } from 'svelte-i18n';
	import { toaster } from '$lib/states/toasterState.svelte';

	interface MarkdownEditorProps {
		content: string;
	}

	let { content = $bindable() }: MarkdownEditorProps = $props();

	const Tab = {
		EDIT: 'edit',
		PREVIEW: 'preview'
	} as const;

	type Tab = (typeof Tab)[keyof typeof Tab];

	let activeTab: Tab = $state(Tab.EDIT);

	const insertSyntax = (syntax: string) => {
		const textarea = document.getElementById('editor') as HTMLTextAreaElement;
		const start = textarea.selectionStart;
		const end = textarea.selectionEnd;
		const selectedText = content.substring(start, end);

		content =
			content.substring(0, start) +
			syntax.replace('{{selection}}', selectedText) +
			content.substring(end);

		const newCursorPos = start + syntax.indexOf('{{selection}}') + selectedText.length;
		setTimeout(() => {
			textarea.setSelectionRange(newCursorPos, newCursorPos);
			textarea.focus();
		}, 0);
	};

	const previewContent = async (): Promise<string> => {
		const parsed = await marked.parse(content);
		return DOMPurify.sanitize(parsed);
	};

	const throwError = (message: string) => {
		console.error('Error:', message);
		toaster.create({
			title: $_('error.title'),
			description: message,
			type: 'error'
		});
	};
</script>

<div class="relative h-full rounded-lg border dark:border-gray-700 dark:bg-gray-900">
	<div class="flex border-b dark:border-gray-700">
		<button
			class={`px-4 py-2 text-sm font-medium ${
				activeTab === Tab.EDIT
					? 'border-b-2 border-blue-600 text-blue-600 dark:border-blue-400 dark:text-blue-400'
					: 'text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200'
			}`}
			onclick={() => (activeTab = Tab.EDIT)}
		>
			{$_('common.edit')}
		</button>
		<button
			class={`px-4 py-2 text-sm font-medium ${
				activeTab === Tab.PREVIEW
					? 'border-b-2 border-blue-600 text-blue-600 dark:border-blue-400 dark:text-blue-400'
					: 'text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200'
			}`}
			onclick={() => (activeTab = Tab.PREVIEW)}
		>
			{$_('common.preview')}
		</button>
		{#if activeTab === Tab.EDIT}
			<Toolbar {insertSyntax} />
		{/if}
	</div>

	{#if activeTab === Tab.EDIT}
		<textarea
			id="editor"
			bind:value={content}
			class="h-[calc(100%-44px)] min-h-[300px] w-full resize-none bg-white p-4 text-gray-900 focus:outline-none dark:bg-gray-900 dark:text-gray-100"
			placeholder={$_('markdownEditor.placeholder')}
		>
		</textarea>
	{/if}
	{#if activeTab === Tab.PREVIEW}
		{#await previewContent()}
			<div class="p-4">{$_('common.loading')}</div>
		{:then safeHtml}
			<div class="prose dark:prose-invert h-[calc(100%-44px)] max-w-none overflow-y-auto p-4">
				<!-- eslint-disable-next-line svelte/no-at-html-tags -->
				{@html safeHtml}
			</div>
		{:catch error}
			{(() => {
				throwError(error.message);
				return '';
			})()}
			<div class="prose dark:prose-invert h-[calc(100%-44px)] max-w-none overflow-y-auto p-4">
				{$_('error.description', {
					values: { status: 'unknown' }
				})}
			</div>
		{/await}
	{/if}
</div>
