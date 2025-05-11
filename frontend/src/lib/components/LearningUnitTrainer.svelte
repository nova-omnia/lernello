<script lang="ts">
	import { blockActionState } from '$lib/states/blockActionState.svelte';
	import BlockItem from '$lib/components/blocks/BlockItem.svelte';
	import { type RoleType } from '$lib/schemas/response/UserInfo';

	interface LearningUnitTrainingContainerProps {
		role: RoleType;
	}
	const { role }: LearningUnitTrainingContainerProps = $props();
	const language = 'ENGLISH'; // Assuming English is the default language for this example

</script>

<div
	class="preset-filled-surface-50-950 border-surface-100-900 m-0 space-y-4 overflow-y-auto border-r p-4"
>
	<div class="space-y-2">
		{#each blockActionState.blocks as block (block.uuid)}
			<div class="space-y-2">
				<BlockItem {block} {role} />
			</div>
		{/each}
	</div>
</div>
