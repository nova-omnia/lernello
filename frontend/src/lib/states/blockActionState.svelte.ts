import type { BlockRes } from '$lib/schemas/response/BlockRes';
import type { BlockAction, BlockActionWithQuickAdd } from '$lib/schemas/request/BlockAction';

let currTempId = 0;
function getTempId() {
	// Generate a temporary ID for new blocks
	const tempId = `tempid:${currTempId}`;
	currTempId++;
	return tempId;
}

let learningUnitBlocksState: BlockRes[] = $state([]);
let blockActionQueue: BlockAction[] = $state([]);
export const blockActionState = {
	get blocks() {
		return learningUnitBlocksState;
	},
	setBlocks(blocks: BlockRes[]) {
		learningUnitBlocksState = blocks;
	},
	get queue() {
		return blockActionQueue;
	},
	clearQueue() {
		blockActionQueue = [];
	}
};

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
				uuid: action.blockId
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

export function queueBlockAction(action: BlockActionWithQuickAdd) {
	let parsedAction: BlockAction;
	if (action.type === 'ADD_BLOCK') {
		parsedAction = {
			...action,
			blockId: getTempId()
		};
	} else {
		parsedAction = action;
	}
	learningUnitBlocksState = applyBlockAction(parsedAction, learningUnitBlocksState);
	blockActionQueue = [...blockActionQueue, parsedAction];
	const blockActionEvent: CustomBlockActionEvent = new CustomEvent('blockAction', {
		detail: {
			action: parsedAction
		}
	});
	document.dispatchEvent(blockActionEvent);
}
