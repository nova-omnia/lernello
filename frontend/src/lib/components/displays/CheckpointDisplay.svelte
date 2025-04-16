<script lang="ts">
	import { _ } from 'svelte-i18n';
	import KitContentItem from './KitContentItem.svelte';

	let showDeleteDialog = $state(false);
</script>

<KitContentItem
	name={$_('learningKit.checkpoint')}
	description={$_('learningKit.checkpoint.description')}
>
	{#snippet actions()}
		<button
			type="button"
			class="btn preset-filled-error-500 rounded-full"
			onclick={(e) => {
				e.preventDefault();
				showDeleteDialog = true;
			}}
		>
			{$_('button.delete')}
		</button>
	{/snippet}
</KitContentItem>
