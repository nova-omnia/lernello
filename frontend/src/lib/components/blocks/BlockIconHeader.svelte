<script lang="ts">
	import { _ } from 'svelte-i18n';
	import {
		type BlockRes,
		MULTIPLE_CHOICE_BLOCK_TYPE,
		QUESTION_BLOCK_TYPE,
		THEORY_BLOCK_TYPE
	} from '$lib/schemas/response/BlockRes';
	import BlockIcon from './BlockIcon.svelte';
	import { INSTRUCTOR_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';
	import BlockAiGenerationButton from '$lib/components/blocks/BlockAiGenerationButton.svelte';
	import { queueBlockAction } from '$lib/states/blockActionState.svelte';
	import { ActionType } from '$lib/schemas/request/block/BlockAction';
	import { toaster } from '$lib/states/toasterState.svelte';
	import { createDebounced } from '$lib/utils/createDebounced';

	interface BlockIconHeaderProps {
		block: BlockRes;
		role: RoleType;
	}
	const { block, role }: BlockIconHeaderProps = $props();
	let name = $derived(block.name);
	let isEditing = $state(false);

	let blockTypeTerm = $derived.by(() => {
		switch (block.type) {
			case THEORY_BLOCK_TYPE:
				return 'block.theoryBlock';
			case MULTIPLE_CHOICE_BLOCK_TYPE:
				return 'block.multipleChoiceQuiz';
			case QUESTION_BLOCK_TYPE:
				return 'block.questionBlock';
			default:
				return 'Unknown Block';
		}
	});

	const enableEditing = () => {
		if (role === INSTRUCTOR_ROLE) {
			isEditing = true;
		}
	};

	const disableEditing = () => {
		onUpdateHandler(name);
		isEditing = false;
	};

	const onUpdateHandler = createDebounced((newName: string) => {
		if (newName.length < 3 || newName.length > 40) {
			toaster.create({
				title: $_('common.warning.title'),
				description: $_('block.newName.danger'),
				type: 'warning'
			});
			return;
		}
		if (newName !== block.name) {
			queueBlockAction({
				type: ActionType.Enum.UPDATE_BLOCK_NAME,
				blockId: block.uuid,
				newName: newName
			});
			name = newName;
		}
	}, 500);

	const handleInputKeydown = (event: KeyboardEvent) => {
		if (event.key === 'Enter') {
			event.preventDefault();
			disableEditing();
		} else if (event.key === 'Escape') {
			name = block.name;
			isEditing = false;
		}
	};
</script>

<div class="flex justify-between">
	<div class="flex items-center gap-2">
		<BlockIcon iconType={block.type} />
		<div class="flex items-baseline gap-2">
			{#if role === INSTRUCTOR_ROLE && isEditing}
				<label class="label">
					<input
						class="input"
						type="text"
						placeholder={$_('block.name')}
						bind:value={name}
						onblur={disableEditing}
						onkeydown={handleInputKeydown}
					/>
				</label>
			{:else}
				<h3
					class="font-medium {role === INSTRUCTOR_ROLE
						? 'hover:bg-surface-100 dark:hover:bg-surface-700 -m-1 cursor-pointer rounded-md p-1'
						: ''}"
					onclick={enableEditing}
					onkeydown={(e) => {
						if (e.key === 'Enter' || e.key === ' ') {
							e.preventDefault();
							enableEditing();
						}
					}}
					role={role === INSTRUCTOR_ROLE ? 'button' : undefined}
				>
					{name}
				</h3>
			{/if}
			<span class="text-sm text-gray-500">({$_(blockTypeTerm)})</span>
		</div>
	</div>

	{#if role === INSTRUCTOR_ROLE}
		<div class="ml-auto">
			<BlockAiGenerationButton {block} />
		</div>
	{/if}
</div>
