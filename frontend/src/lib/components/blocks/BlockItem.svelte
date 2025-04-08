<script lang="ts">
	import TheoryBlockComponent from './BlockTheoryItem.svelte';
	import QuizBlockComponent from './BlockQuizItem.svelte';
	import type { BlockRes } from '$lib/schemas/response/BlockRes';
	import BlockIconHeader from './BlockIconHeader.svelte';

	interface BlockItemProps {
		block: BlockRes;
	}
	const { block }: BlockItemProps = $props();

	// TODO: Improve mapping similar to BlockIcon.svelte
	let Component = block.type === 'THEORY' ? TheoryBlockComponent : QuizBlockComponent;
</script>

<div
	class="card bg-surface-100-900 border-surface-200-800 space-y-5 border p-4 shadow transition-all duration-200"
>
	<!-- Block Header -->
	<BlockIconHeader {block} />
	<!-- Block Content -->
	<Component />
</div>
