<script lang="ts">
	import { Plus } from 'lucide-svelte';

	interface LearningKitProps {
		title: string;
	}

	const { title }: LearningKitProps = $props();
</script>

<a
	class="text-surface-700-300 card preset-filled-surface-50-950 border-surface-300-700 hover:preset-filled-surface-100-900 hover:text-surface-700-300 flex h-36 w-full max-w-52 flex-col items-center justify-center space-y-2 border border-dotted p-4 text-center"
	href="/learningkit/create-form"
>
	<Plus size={32} />
	<p>{title}</p>
</a>
