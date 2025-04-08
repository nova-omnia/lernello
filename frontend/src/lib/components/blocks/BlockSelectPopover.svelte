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
	import { PlusCircle } from 'lucide-svelte';
	import { fade } from 'svelte/transition';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import { BlockType } from '$lib/schemas/request/CreateBlock';
	import { ActionType } from '$lib/schemas/request/BlockAction';
	import { AddBlockActionSchema } from '$lib/schemas/request/BlockAction';

	interface BlockSelectPopoverProps {
		index: number;
	}
	const { index }: BlockSelectPopoverProps = $props();
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
				learningUnitId: '00000000-0000-0000-0000-000000000000', // Placeholder id
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
				learningUnitId: '00000000-0000-0000-0000-000000000000', // Placeholder id
				question: 'placeholder question?',
				possibleAnswers: ['A', 'B'],
				correctAnswers: ['A']
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
					Theory Block
				</button>
				<button
					type="button"
					class="btn preset-filled-primary-500 block w-full"
					onclick={() => {
						addMultipleChoiceBlock();
					}}
				>
					Multiple Choice Quiz
				</button>
				<button
					type="button"
					class="btn preset-filled-primary-500 block w-full"
					onclick={() => {
						//TODO: Implement Text Answer Quiz
						alert(
							'Text Answer Quiz is not implemented yet. Please use the Multiple Choice Quiz for now.'
						);
					}}
				>
					Text Answer Quiz
				</button>
			</div>
			<FloatingArrow bind:ref={elemArrow} context={floating.context} fill="#575969" />
		</div>
	{/if}
</div>
