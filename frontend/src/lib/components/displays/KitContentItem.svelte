<script lang="ts">
	import { AlignLeft, GripVertical } from 'lucide-svelte';
	import type { Snippet } from 'svelte';

	interface KitContentItemProps {
		name: string;
		description?: string;
		actions: Snippet;
	}
	const { name, description, actions }: KitContentItemProps = $props();
</script>

<div class="flex items-center">
	<GripVertical color="gray" class="h-10 w-10" />
	<div
		class="card preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center justify-between p-3"
	>
		<div class="flex gap-4">
			<AlignLeft class="size-10" />
			<div class="flex flex-col justify-center">
				<p class="text-xs font-bold">{name}</p>
				<p class="text-xs">{description}</p>
			</div>
		</div>

		<div class="flex gap-2">
			{@render actions()}
		</div>
	</div>
</div>
