<script lang="ts">
	import { Code, Sigma, Bold, Italic, List, ListOrdered } from 'lucide-svelte';

	interface ToolbarProps {
		insertSyntax: (syntax: string) => void;
	}
	const { insertSyntax }: ToolbarProps = $props();
</script>

<div class="absolute right-0 top-0 flex items-center space-x-2 p-2">
	<button
		onclick={() => insertSyntax('**{{selection}}**')}
		class="p-1 hover:bg-gray-100 dark:hover:bg-gray-800 rounded"
		title="Bold"
	>
		<Bold class="w-5 h-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		onclick={() => insertSyntax('*{{selection}}*')}
		class="p-1 hover:bg-gray-100 dark:hover:bg-gray-800 rounded"
		title="Italic"
	>
		<Italic class="w-5 h-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		onclick={() => insertSyntax('```\n{{selection}}\n```')}
		class="p-1 hover:bg-gray-100 dark:hover:bg-gray-800 rounded"
		title="Code block"
	>
		<Code class="w-5 h-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		onclick={() => insertSyntax('$$\n{{selection}}\n$$')}
		class="p-1 hover:bg-gray-100 dark:hover:bg-gray-800 rounded"
		title="LaTeX math"
	>
		<Sigma class="w-5 h-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		onclick={() => insertSyntax('- {{selection}}')}
		class="p-1 hover:bg-gray-100 dark:hover:bg-gray-800 rounded"
		title="Unordered list"
	>
		<List class="w-5 h-5 text-gray-600 dark:text-gray-300" />
	</button>
	<button
		onclick={() => insertSyntax('1. {{selection}}')}
		class="p-1 hover:bg-gray-100 dark:hover:bg-gray-800 rounded"
		title="Ordered list"
	>
		<ListOrdered class="w-5 h-5 text-gray-600 dark:text-gray-300" />
	</button>
</div>