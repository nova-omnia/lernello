<script lang="ts">
	import { Code, Bold, Italic, List, ListOrdered } from 'lucide-svelte';

	interface ToolbarProps {
		insertSyntax: (syntax: string) => void;
	}
	const { insertSyntax }: ToolbarProps = $props();
</script>

<div class="absolute right-0 top-0 flex items-center space-x-2 p-2">
	<button
		type="button"
		onclick={() => insertSyntax('**{{selection}}**')}
		class="btn preset-tonal-surface hover:"
		title="Bold"
	>
		<Bold class="w-5 h-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('*{{selection}}*')}
		class="btn preset-tonal-surface hover:"
		title="Italic"
	>
		<Italic class="w-5 h-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('```\n{{selection}}\n```')}
		class="btn preset-tonal-surface hover:"
		title="Code block"
	>
		<Code class="w-5 h-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('- {{selection}}')}
		class="btn preset-tonal-surface hover:"
		title="Unordered list"
	>
		<List class="w-5 h-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		type="button"
		onclick={() => insertSyntax('1. {{selection}}')}
		class="btn preset-tonal-surface hover:"
		title="Ordered list"
	>
		<ListOrdered class="w-5 h-5 text-gray-600 dark:text-gray-300" />
	</button>
</div>