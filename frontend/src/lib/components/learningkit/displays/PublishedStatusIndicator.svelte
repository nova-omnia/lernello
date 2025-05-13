<script lang="ts">
	import { _ } from 'svelte-i18n';

	interface Props {
		published: boolean;
		size?: string;
	}
	const { published, size = 'h-3 w-3' }: Props = $props();
</script>

<div
	class="{size} rounded-full {published ? 'bg-green-500' : 'bg-gray-400'}"
	title={published
		? $_('learningKit.publishStatus.published')
		: $_('learningKit.publishStatus.unpublished')}
></div>
