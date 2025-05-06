<script lang="ts">
	import {
		FloatingArrow,
		arrow,
		autoUpdate,
		flip,
		offset,
		useClick,
		useDismiss,
		useFloating,
		useInteractions,
		useRole
	} from '@skeletonlabs/floating-ui-svelte';
	import { _ } from 'svelte-i18n';
	import { BookOpen, FileText, ListChecks, PlusCircle } from 'lucide-svelte';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import { BlockType } from '$lib/schemas/request/block/CreateBlock';
	import { ActionType } from '$lib/schemas/request/block/BlockAction';
	import { AddBlockActionSchema } from '$lib/schemas/request/block/BlockAction';

	interface BlockSelectPopoverProps {
		index: number;
		learningUnitId: string;
	}
	const { index, learningUnitId }: BlockSelectPopoverProps = $props();
	const insertIndex = index + 1;

	// State
	let open = $state(false);
	let elemArrow: HTMLElement | null = $state(null);

	// Use Floating
	const floating = useFloating({
		whileElementsMounted: autoUpdate,
		get open() {
			return open;
		},
		onOpenChange: (v) => {
			open = v;
		},
		placement: 'top',
		get middleware() {
			return [offset(10), flip(), elemArrow && arrow({ element: elemArrow })];
		}
	});

	// Interactions
	const role = useRole(floating.context);
	const click = useClick(floating.context);
	const dismiss = useDismiss(floating.context);
	const interactions = useInteractions([role, click, dismiss]);

	const simulateEscKeyPress = () => {
		// simulate esc key press to close the popover after action
		document.dispatchEvent(
			new KeyboardEvent('keydown', {
				key: 'Escape',
				code: 'Escape'
			})
		);
	};

	function addTheoryBlock() {
		const actionData = {
			type: ActionType.Enum.ADD_BLOCK,
			blockId: '',
			index: insertIndex,
			data: {
				type: BlockType.Enum.THEORY,
				name: 'Theory',
				position: 0,
				learningUnitId: learningUnitId,
				content: 'placeholder'
			}
		};
		const parsedAction = AddBlockActionSchema.parse(actionData);
		queueBlockAction(parsedAction);
	}

	function addMultipleChoiceBlock() {
		const actionData = {
			type: ActionType.Enum.ADD_BLOCK,
			blockId: '',
			index: insertIndex,
			data: {
				type: BlockType.Enum.MULTIPLE_CHOICE,
				name: 'Multiple Choice',
				position: 0,
				learningUnitId: learningUnitId,
				question: 'placeholder question?',
				possibleAnswers: ['A', 'B'],
				correctAnswers: ['A']
			}
		};
		const parsedAction = AddBlockActionSchema.parse(actionData);
		queueBlockAction(parsedAction);
	}

	function addQuestionBlock() {
		const actionData = {
			type: ActionType.Enum.ADD_BLOCK,
			blockId: '',
			index: insertIndex,
			data: {
				type: BlockType.Enum.QUESTION,
				name: 'Question',
				position: 0,
				learningUnitId: learningUnitId,
				question: 'placeholder question?',
				expectedAnswer: 'placeholder answer'
			}
		};
		const parsedAction = AddBlockActionSchema.parse(actionData);
		queueBlockAction(parsedAction);
	}
</script>

<div class="flex items-center gap-2 p-2">
	<!-- Reference Element -->
	<hr class="hr border-t-2 border-dashed border-gray-400" />
	<button
		bind:this={floating.elements.reference}
		{...interactions.getReferenceProps()}
		class="btn text-gray-400"
	>
		<PlusCircle class="h-5 w-5" />
	</button>
	<hr class="hr border-t-2 border-dashed border-gray-400" />
	<!-- Floating Element -->
	{#if open}
		<div
			bind:this={floating.elements.floating}
			style={floating.floatingStyles}
			{...interactions.getFloatingProps()}
			class="floating"
		>
			<div
				class="preset-filled-surface-100-900 card border-surface-200-800 border text-nowrap"
				role="button"
				tabindex="0"
				onkeydown={simulateEscKeyPress}
				onclick={simulateEscKeyPress}
				{...interactions.getFloatingProps()}
			>
				<nav class="btn-group">
					<button
						type="button"
						class="btn preset-filled-primary-500"
						onclick={() => {
							addTheoryBlock();
						}}
					>
						<BookOpen />
						{$_('block.theoryBlock')}
					</button>
					<button
						type="button"
						class="btn preset-filled-primary-500"
						onclick={() => {
							addMultipleChoiceBlock();
						}}
					>
						<ListChecks />
						{$_('block.multipleChoiceQuiz')}
					</button>
					<button
						type="button"
						class="btn preset-filled-primary-500"
						onclick={() => {
							addQuestionBlock();
						}}
					>
						<FileText />
						{$_('block.questionBlock')}
					</button>
				</nav>
			</div>
			<FloatingArrow bind:ref={elemArrow} context={floating.context} fill="#575969" />
		</div>
	{/if}
</div>
