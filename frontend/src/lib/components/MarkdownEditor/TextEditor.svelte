<script lang="ts">
	import { marked } from 'marked';
	import DOMPurify from 'dompurify';
	import Toolbar from './Toolbar.svelte';
	import { _ } from 'svelte-i18n';
	import { toaster } from '$lib/states/toasterState.svelte';
	import { INSTRUCTOR_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';
	import EditPreviewTabContainer from '$lib/components/blocks/EditPreviewTabContainer.svelte';
	import {
		handleMarkdownSyntaxInsertion,
		handleEnterKeyForListsAndBlockquotes
	} from './markdownEditorLogic';

	interface TextEditorProps {
		content: string;
		onUpdate?: (content: string) => void;
		role: RoleType;
	}

	let { content: initialContent, onUpdate, role }: TextEditorProps = $props();
	let content = $state(initialContent);
	let lastSavedContent = $state(initialContent);
	let editorRef: HTMLTextAreaElement | undefined = $state();

	let pageScrollY = $derived(0);
	let editorScrollTop = $derived(0);

	$effect(() => {
		if (onUpdate && content !== lastSavedContent) {
			onUpdate(content);
			lastSavedContent = content;
		}
	});

	$effect(() => {
		content = initialContent;
		lastSavedContent = initialContent;
	});

	const saveScrollPositions = () => {
		if (editorRef) {
			editorScrollTop = editorRef.scrollTop;
		}
		pageScrollY = window.scrollY;
	};

	const restoreScrollPositionsAndFocus = (newSelectionStart: number, newSelectionEnd: number) => {
		setTimeout(() => {
			if (editorRef) {
				window.scrollTo(window.scrollX, pageScrollY);

				editorRef.scrollTop = editorScrollTop;
				editorRef.focus();
				editorRef.setSelectionRange(newSelectionStart, newSelectionEnd);
			}
		}, 0);
	};

	const insertSyntax = (
		syntaxString: string,
		type: 'heading' | 'codeblock' | 'list' | 'blockquote' | 'inline'
	) => {
		const textarea = editorRef;
		if (!textarea) {
			toaster.create({
				description: $_('textEditor.error.editorNotAvailable'),
				type: 'error'
			});
			return;
		}

		saveScrollPositions(); // <-- Save scroll positions BEFORE content change

		const { selectionStart, selectionEnd } = textarea;
		const result = handleMarkdownSyntaxInsertion(
			content,
			selectionStart,
			selectionEnd,
			syntaxString,
			type
		);

		if (result) {
			content = result.newContent;
			restoreScrollPositionsAndFocus(result.newSelectionStart, result.newSelectionEnd);
		} else {
			toaster.create({
				description: $_('textEditor.error.syntaxError'),
				type: 'error'
			});
		}
	};

	const handleKeyDown = (event: KeyboardEvent) => {
		if (event.key === 'Enter') {
			const textarea = editorRef;
			if (!textarea) return;

			saveScrollPositions();

			const { selectionStart, selectionEnd } = textarea;
			const result = handleEnterKeyForListsAndBlockquotes(content, selectionStart, selectionEnd);

			if (result) {
				if (result.preventDefault) {
					event.preventDefault();
				}
				content = result.newContent;
				restoreScrollPositionsAndFocus(result.newSelectionStart, result.newSelectionEnd);
			}
		}
	};

	const previewContent = async (): Promise<string> => {
		const parsed = await marked.parse(content, { breaks: true });
		return DOMPurify.sanitize(parsed);
	};

	const throwError = (message: string) => {
		console.error('Error:', message);
		toaster.create({
			description: message,
			type: 'error'
		});
	};
</script>

<EditPreviewTabContainer {role}>
	{#snippet editHeaderContent()}
		{#if role === INSTRUCTOR_ROLE}
			<Toolbar {insertSyntax} />
		{/if}
	{/snippet}

	{#snippet edit()}
		<textarea
			bind:this={editorRef}
			bind:value={content}
			onkeydown={handleKeyDown}
			class="textarea focus:border-primary-500 dark:focus:border-primary-500 block h-[300px] min-h-[300px] w-full resize-none rounded-md border border-gray-300 bg-transparent p-2.5 text-gray-900 focus:ring-0 focus:outline-none dark:border-gray-600 dark:text-white dark:placeholder-gray-400"
			placeholder={$_('markdownEditor.placeholder')}
		></textarea>
	{/snippet}

	{#snippet preview()}
		{#await previewContent()}
			<div class="text-center text-gray-500 dark:text-gray-400">{$_('common.loading')}...</div>
		{:then safeHtml}
			<div class="prose dark:prose-invert max-w-none" style="min-height: 300px;">
				<!-- eslint-disable-next-line svelte/no-at-html-tags -->
				{@html safeHtml}
			</div>
		{:catch error}
			{(() => {
				throwError(error.message);
				return '';
			})()}
			<div class="prose dark:prose-invert max-w-none text-red-600 dark:text-red-400">
				{$_('error.description', { values: { status: 'Preview Generation' } })}
				<pre class="mt-2 text-xs">{error.message}</pre>
			</div>
		{/await}
	{/snippet}
</EditPreviewTabContainer>
