import type { BlockRes } from '$lib/schemas/response/BlockRes';
import {
	ActionType,
	type BlockAction,
	type BlockActionWithQuickAdd
} from '$lib/schemas/request/BlockAction';
import { BlockType } from '$lib/schemas/request/CreateBlock';

let currTempId = 0;
function getTempId() {
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
			let newBlock: BlockRes;

			if (action.data.type === BlockType.Enum.THEORY) {
				newBlock = {
					type: action.data.type,
					name: action.data.name,
					uuid: action.blockId,
					position: action.data.position || 0,
					content: action.data.content || 'placeholder'
				};
			} else if (action.data.type === BlockType.Enum.MULTIPLE_CHOICE) {
				newBlock = {
					type: action.data.type,
					name: action.data.name,
					uuid: action.blockId,
					position: action.data.position || 0,
					question: action.data.question || 'placeholder question',
					possibleAnswers: action.data.possibleAnswers || [],
					correctAnswers: action.data.correctAnswers || []
				};
			} else {
				throw new Error('Unsupported block type.');
			}

			if (action.index !== undefined) {
				blocks.splice(action.index, 0, newBlock);
			} else {
				blocks.push(newBlock);
			}
			break;
		}

		case 'REORDER_BLOCK': {
			const blockToMove = blocks.find((block) => block.uuid === action.blockId);
			if (blockToMove) {
				blocks = blocks.filter((block) => block.uuid !== action.blockId);
				blocks.splice(action.newIndex, 0, blockToMove);
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

	if (action.type === ActionType.Enum.ADD_BLOCK) {
		if (action.data.type === BlockType.Enum.THEORY) {
			parsedAction = {
				type: 'ADD_BLOCK',
				index: action.index,
				blockId: getTempId(),
				data: {
					type: action.data.type,
					name: action.data.name,
					position: action.data.position,
					learningUnitId: action.data.learningUnitId || 'FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF', // Placeholder id
					content: action.data.content || 'placeholder'
				}
			};
		} else if (action.data.type === BlockType.Enum.MULTIPLE_CHOICE) {
			parsedAction = {
				type: 'ADD_BLOCK',
				index: action.index,
				blockId: getTempId(),
				data: {
					type: action.data.type,
					name: action.data.name,
					position: action.data.position,
					learningUnitId: action.data.learningUnitId || 'FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF', // Placeholder id
					question: action.data.question || 'placeholder question',
					possibleAnswers: action.data.possibleAnswers || [],
					correctAnswers: action.data.correctAnswers || []
				}
			};
		} else {
			throw new Error('Unsupported block type.');
		}
	} else if (action.type === ActionType.Enum.REORDER_BLOCK) {
		parsedAction = {
			type: 'REORDER_BLOCK',
			blockId: action.blockId,
			newIndex: action.newIndex
		};
	} else {
		parsedAction = action;
	}

	learningUnitBlocksState = applyBlockAction(parsedAction, learningUnitBlocksState);
	blockActionQueue = [...blockActionQueue, parsedAction];

	const blockActionEvent: CustomBlockActionEvent = new CustomEvent('blockAction', {
		detail: { action: parsedAction }
	});
	document.dispatchEvent(blockActionEvent);
}
