<script lang="ts">
	import { Code, Bold, Italic, List, ListOrdered } from 'lucide-svelte';

	interface ToolbarProps {
		insertSyntax: (syntax: string) => void;
	}
	const { insertSyntax }: ToolbarProps = $props();
</script>

<div class="absolute top-0 right-0 flex items-center space-x-2 p-2">
	<button
		type="button"
		onclick={() => insertSyntax('**{{selection}}**')}
		class="btn preset-tonal-surface hover:"
		title="Bold"
	>
		<Bold class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('*{{selection}}*')}
		class="btn preset-tonal-surface hover:"
		title="Italic"
	>
		<Italic class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('```\n{{selection}}\n```')}
		class="btn preset-tonal-surface hover:"
		title="Code block"
	>
		<Code class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('- {{selection}}')}
		class="btn preset-tonal-surface hover:"
		title="Unordered list"
	>
		<List class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('1. {{selection}}')}
		class="btn preset-tonal-surface hover:"
		title="Ordered list"
	>
		<ListOrdered class="h-5 w-5 text-gray-600 dark:text-gray-300" />
	</button>
</div>
