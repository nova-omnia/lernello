import type { BlockRes } from '$lib/schemas/response/BlockRes';
import type { BlockAction } from '$lib/schemas/request/blockAction';

let currTempId = 0;
function getTempId() {
	// Generate a temporary ID for new blocks
	const tempId = `tempid:${currTempId}`;
	currTempId++;
	return tempId;
}

type BlockActionState = {
	queue: BlockAction[];
	blocks: BlockRes[];
};

export const blockActionState: BlockActionState = $state({
	queue: [],
	blocks: []
});

export type CustomBlockActionEvent = CustomEvent<{
	action: BlockAction;
}>;
export function addBlockActionListener(onBlockAction: (event: CustomBlockActionEvent) => void) {
	const handler = (e: Event) => {
		if (e instanceof CustomEvent) {
			onBlockAction(e as CustomBlockActionEvent);
		}
	};
	document.addEventListener('blockAction', handler);

	return {
		remove: () => {
			document.removeEventListener('blockAction', handler);
		}
	};
}

function applyBlockAction(action: BlockAction, blocks: BlockRes[]): BlockRes[] {
	switch (action.type) {
		case 'ADD_BLOCK': {
			const newBlock = {
				type: action.data.type,
				name: action.data.name,
				uuid: getTempId()
			};

			if (action.data.index !== undefined) {
				blocks.splice(action.data.index, 0, newBlock);
			} else {
				blocks.push(newBlock);
			}
			break;
		}

		case 'REORDER_BLOCK': {
			const blockToMove = blocks.find((block) => block.uuid === action.blockId);
			if (blockToMove) {
				blocks = blocks.filter((block) => block.uuid !== action.blockId);
				blocks.splice(action.data.newIndex, 0, blockToMove);
			}
			break;
		}

		case 'REMOVE_BLOCK': {
			blocks = blocks.filter((block) => block.uuid !== action.blockId);
			break;
			}

		default:
			throw new Error(`Unknown action type`);
	}

	return blocks;
}

export function queueBlockAction(action: BlockAction) {
	blockActionState.blocks = applyBlockAction(action, blockActionState.blocks);
	blockActionState.queue = [...blockActionState.queue, action];
	const blockActionEvent: CustomBlockActionEvent = new CustomEvent('blockAction', {
		detail: {
			action
		}
	});
	document.dispatchEvent(blockActionEvent);
}
