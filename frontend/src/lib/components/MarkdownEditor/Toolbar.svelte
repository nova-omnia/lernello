<script lang="ts">
	import {
		Code,
		Bold,
		Italic,
		List,
		ListOrdered,
		TextQuote,
		Strikethrough,
		Link,
		Heading,
		Underline
	} from 'lucide-svelte';
	import { _ } from 'svelte-i18n';

	interface ToolbarProps {
		insertSyntax: (syntax: string) => void;
	}
	const { insertSyntax }: ToolbarProps = $props();
</script>

<div class="flex h-6 items-center space-x-2">
	<button
		type="button"
		onclick={() => insertSyntax('### {{selection}}')}
		class="btn preset-tonal-surface hover:"
		title={$_('textEditor.toolbar.heading')}
	>
		<Heading class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('**{{selection}}**')}
		class="btn preset-tonal-surface hover:"
		title={$_('textEditor.toolbar.bold')}
	>
		<Bold class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('*{{selection}}*')}
		class="btn preset-tonal-surface hover:"
		title={$_('textEditor.toolbar.italic')}
	>
		<Italic class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('__{{selection}}__')}
		class="btn preset-tonal-surface hover:"
		title={$_('textEditor.toolbar.underline')}
	>
		<Underline class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('~~{{selection}}~~')}
		class="btn preset-tonal-surface hover:"
		title={$_('textEditor.toolbar.strikethrough')}
	>
		<Strikethrough class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>

	<span class="vr border-gray-200 dark:border-gray-700"></span>

	<button
		type="button"
		onclick={() => insertSyntax('[{{selection}}](url)')}
		class="btn preset-tonal-surface hover:"
		title={$_('textEditor.toolbar.link')}
	>
		<Link class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('```\n{{selection}}\n```')}
		class="btn preset-tonal-surface hover:"
		title={$_('textEditor.toolbar.codeBlock')}
	>
		<Code class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>

	<span class="vr border-gray-200 dark:border-gray-700"></span>

	<button
		type="button"
		onclick={() => insertSyntax('- {{selection}}')}
		class="btn preset-tonal-surface hover:"
		title={$_('textEditor.toolbar.list')}
	>
		<List class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('1. {{selection}}')}
		class="btn preset-tonal-surface hover:"
		title={$_('textEditor.toolbar.orderedList')}
	>
		<ListOrdered class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('> {{selection}}')}
		class="btn preset-tonal-surface hover:"
		title={$_('textEditor.toolbar.quote')}
	>
		<TextQuote class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
</div>
