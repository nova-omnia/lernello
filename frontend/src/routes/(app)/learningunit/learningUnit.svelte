<script lang="ts">
	import { overrideItemIdKeyNameBeforeInitialisingDndZones } from 'svelte-dnd-action';
	overrideItemIdKeyNameBeforeInitialisingDndZones('uuid');
	import TheoryBlockComponent from './blocks/theoryBlockComponent.svelte';
	import QuizBlockComponent from './blocks/quizBlockComponent.svelte';
	import { type BlockItem } from './blocks/globalBlocks';
	import LeftColumn from './columns/leftColumn.svelte';
	import MiddleColumn from './columns/middleColumn.svelte';
	import RightColumn from './columns/rightColumn.svelte';

	const flipDurationMs = 200;

	//TODO: Fetch blocks from API and assign the component based on the type

	let blocks: BlockItem[] = [
		{ uuid: '1', name: 'Block 1', type: 'theory', element: TheoryBlockComponent },
		{ uuid: '2', name: 'Block 2', type: 'quiz', element: QuizBlockComponent },
		{ uuid: '3', name: 'Block 3', type: 'theory', element: TheoryBlockComponent },
		{ uuid: '4', name: 'Block 4', type: 'quiz', element: QuizBlockComponent }
	];
	// TODO: Fetch unitID
	let unitId = '1';
</script>

<div class="grid h-screen grid-cols-[25%_50%_25%]">
	<LeftColumn {blocks} {unitId} />

	<MiddleColumn {blocks} {flipDurationMs} {unitId} />

	<RightColumn {blocks} {flipDurationMs} {unitId} />
</div>
