<script lang="ts">
	import { marked } from 'marked';
	import Toolbar from './Toolbar.svelte';
	import { _ } from 'svelte-i18n';

	interface TextEditorProps {
		content: string;
	}
	let { content }: TextEditorProps = $props();

	const Tab = {
		EDIT: 'edit',
		PREVIEW: 'preview'
	} as const;

	type Tab = typeof Tab[keyof typeof Tab];

	let activeTab: Tab = Tab.EDIT;

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

	const previewContent = () => {
		return marked.parse(content);
	};
</script>

<div class="relative h-full dark:bg-gray-900 rounded-lg border dark:border-gray-700">

	<div class="flex border-b dark:border-gray-700">
		<button
			class={`px-4 py-2 text-sm font-medium ${
        activeTab === Tab.EDIT
          ? 'text-blue-600 border-b-2 border-blue-600 dark:text-blue-400 dark:border-blue-400'
          : 'text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200'
      }`}
			onclick={() => activeTab = Tab.EDIT}
		>
			{$_('common.edit')}
		</button>
		<button
			class={`px-4 py-2 text-sm font-medium ${
        activeTab === Tab.PREVIEW
          ? 'text-blue-600 border-b-2 border-blue-600 dark:text-blue-400 dark:border-blue-400'
          : 'text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200'
      }`}
			onclick={() => activeTab = Tab.PREVIEW}
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
			class="w-full min-h-[300px] h-[calc(100%-44px)] p-4 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 resize-none focus:outline-none"
			placeholder={$_('markdownEditor.placeholder')}
		>
    </textarea>
	{:else}
		<div class="prose dark:prose-invert max-w-none p-4 h-[calc(100%-44px)] overflow-y-auto">
			{@html previewContent()}
		</div>
	{/if}
</div>