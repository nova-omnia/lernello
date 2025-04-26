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
	import { PlusCircle } from 'lucide-svelte';
	import { fade } from 'svelte/transition';
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
				code: 'Escape',
				keyCode: 27,
				which: 27
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
				name: 'Theory Block',
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
				name: 'Multiple Choice Quiz',
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

	function addMultimediaBlock() {
		const actionData = {
			type: ActionType.Enum.ADD_BLOCK,
			blockId: '',
			index: insertIndex,
			data: {
				type: BlockType.Enum.MULTIMEDIA,
				name: 'Multimedia',
				position: 0,
				learningUnitId: learningUnitId,
				content: 'placeholder'
			}
		};
		const parsedAction = AddBlockActionSchema.parse(actionData);
		queueBlockAction(parsedAction);
	}
</script>

<div>
	<!-- Reference Element -->
	<button
		bind:this={floating.elements.reference}
		{...interactions.getReferenceProps()}
		class="btn w-full"
	>
		<PlusCircle class="h-4 w-4" />
	</button>
	<!-- Floating Element -->
	{#if open}
		<div
			bind:this={floating.elements.floating}
			style={floating.floatingStyles}
			{...interactions.getFloatingProps()}
			class="floating"
			transition:fade={{ duration: 200 }}
		>
			<div
				class="preset-filled-surface-100-900 card border-surface-200-800 space-y-2 border-[1px] p-2 shadow-lg"
				role="button"
				tabindex="0"
				onkeydown={simulateEscKeyPress}
				onclick={simulateEscKeyPress}
				{...interactions.getFloatingProps()}
			>
				<button
					type="button"
					class="btn preset-filled-primary-500 block w-full"
					onclick={() => {
						addTheoryBlock();
					}}
				>
					{$_('block.theoryBlock')}
				</button>
				<button
					type="button"
					class="btn preset-filled-primary-500 block w-full"
					onclick={() => {
						addMultipleChoiceBlock();
					}}
				>
					{$_('block.multipleChoiceQuiz')}
				</button>
				<button
					type="button"
					class="btn preset-filled-primary-500 block w-full"
					onclick={() => {
						addQuestionBlock();
					}}
				>
					{$_('block.questionBlock')}
				</button>
				<button
					type="button"
					class="btn preset-filled-primary-500 block w-full"
					onclick={() => {
						addMultimediaBlock();
					}}
				>
					{$_('block.multimediaBlock')}
				</button>
			</div>
			<FloatingArrow bind:ref={elemArrow} context={floating.context} fill="#575969" />
		</div>
	{/if}
</div>
