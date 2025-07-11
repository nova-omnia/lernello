<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { type BlockRes } from '$lib/schemas/response/BlockRes';
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
		language: string;
		isGenerationLoading: (isLoading: boolean) => void;
	}
	const { block, role, language, isGenerationLoading }: BlockIconHeaderProps = $props();
	const blockName = $derived(
		block?.translatedContents?.find((content) => content.language === language)?.name ?? block.name
	);
	let name = $derived(blockName);

	const onUpdateHandler = createDebounced((newName: string) => {
		if ((newName.length < 3 || newName.length > 40) && newName.trim() !== '') {
			toaster.create({
				description: $_('block.newName.danger'),
				type: 'warning'
			});
			return;
		}
		if (newName !== blockName) {
			queueBlockAction({
				type: ActionType.Enum.UPDATE_BLOCK_NAME,
				blockId: block.uuid,
				newName: newName
			});
		}
	}, 500);

	const handleInputBlur = () => {
		onUpdateHandler(name);
	};

	const handleInputKeydown = (event: KeyboardEvent) => {
		if (event.key === 'Enter') {
			event.preventDefault();
			onUpdateHandler(name);
			(event.target as HTMLElement).blur();
		} else if (event.key === 'Escape') {
			name = block.name;
			(event.target as HTMLElement).blur();
		}
	};
</script>

<div class="flex w-full justify-between">
	<div class="flex items-center gap-2">
		<BlockIcon iconType={block.type} />
		<div class="flex items-baseline gap-2 text-nowrap">
			{#if role === INSTRUCTOR_ROLE}
				<label class="label">
					<input
						class="input -m-1 w-150 p-1 font-medium"
						type="text"
						placeholder={$_('block.name')}
						bind:value={name}
						onblur={handleInputBlur}
						onkeydown={handleInputKeydown}
					/>
				</label>
			{:else}
				<h3 class="font-medium">
					{name}
				</h3>
			{/if}
		</div>
	</div>

	{#if role === INSTRUCTOR_ROLE}
		<div class="ml-auto">
			<BlockAiGenerationButton {block} {isGenerationLoading} />
		</div>
	{/if}
</div>
