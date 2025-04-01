<script lang="ts">
	import { BLOCK_GROUPS, type BlockGroup, type BlockType } from '$lib/models/globalBlock';
	import { addTheoryBlock, addMultipleChoiceQuizBlock, addTextAnswerQuizBlock} from "$lib/api/learningUnits";

	let openSections: Record<BlockType, boolean> = {
		theory: true,
		quiz: true
	};

	const toggleSection = (type: BlockType) => {
		openSections = { ...openSections, [type]: !openSections[type] };
	};

	const blockGroups: BlockGroup[] = BLOCK_GROUPS;
</script>

<div class="bg-gray-30 border-r border-gray-300 p-4">
	<h2 class="mb-4 text-lg font-bold">Actions</h2>

	<button
		type="button"
		class="bg-primary-500 hover:bg-primary-600 w-full rounded-xl px-4 py-2 text-white"
		onclick={addTheoryBlock}
	>
		Theory Block
	</button>

	{#each blockGroups as group (group.type)}
		<div class="mb-4 py-10">
			<button
				class="flex w-full items-center justify-between rounded-t-lg bg-white p-2 hover:bg-gray-200"
				onclick={() => toggleSection(group.type)}
				aria-expanded={openSections[group.type]}
			>
				<span role="heading" aria-level="3" class="font-bold">{group.label}</span>
				<span class="text-lg">{openSections[group.type] ? '−' : '+'}</span>
			</button>

			{#if openSections[group.type]}
				<div class="space-y-2 rounded-b-lg border-t-0 bg-white p-2">
					{#if group.type === 'quiz'}
						<button
							type="button"
							class="bg-primary-500 hover:bg-primary-600 w-full rounded-xl px-4 py-2 text-white"
							onclick={addMultipleChoiceQuizBlock}
						>
							Multiple Choice Quiz
						</button>
						<button
							type="button"
							class="bg-primary-500 hover:bg-primary-600 w-full rounded-xl px-4 py-2 text-white"
							onclick={addTextAnswerQuizBlock}
						>
							Text Answer Quiz
						</button>
					{/if}
				</div>
			{/if}
		</div>
	{/each}
</div>
