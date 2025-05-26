import type { BlockRes } from '$lib/schemas/response/BlockRes';
import {
	ActionType,
	type BlockAction,
	type BlockActionWithQuickAdd,
	type UpdateBlockAction
} from '$lib/schemas/request/block/BlockAction';
import { BlockType } from '$lib/schemas/request/block/CreateBlock';

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
		case ActionType.enum.ADD_BLOCK: {
			let newBlock: BlockRes;

			if (action.data.type === BlockType.Enum.THEORY) {
				newBlock = {
					type: action.data.type,
					name: action.data.name,
					uuid: action.blockId,
					position: action.data.position || 0,
					content: action.data.content || '',
					translatedContents: []
				};
			} else if (action.data.type === BlockType.Enum.MULTIPLE_CHOICE) {
				newBlock = {
					type: action.data.type,
					name: action.data.name,
					uuid: action.blockId,
					position: action.data.position || 0,
					question: action.data.question || 'placeholder question',
					possibleAnswers: action.data.possibleAnswers || [],
					correctAnswers: action.data.correctAnswers || [],
					translatedContents: []
				};
			} else if (action.data.type === BlockType.Enum.QUESTION) {
				newBlock = {
					type: action.data.type,
					name: action.data.name,
					uuid: action.blockId,
					position: action.data.position || 0,
					question: action.data.question || 'placeholder question',
					expectedAnswer: action.data.expectedAnswer || 'placeholder answer',
					translatedContents: []
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

		case ActionType.Enum.REORDER_BLOCK: {
			const blockToMove = blocks.find((block) => block.uuid === action.blockId);
			if (blockToMove) {
				blocks = blocks.filter((block) => block.uuid !== action.blockId);
				blocks.splice(action.newIndex, 0, blockToMove);
			}
			break;
		}

		case ActionType.Enum.REMOVE_BLOCK: {
			blocks = blocks.filter((block) => block.uuid !== action.blockId);
			break;
		}

		case ActionType.Enum.UPDATE_BLOCK: {
			return blocks.map((block) => {
				if (block.uuid === action.blockId) {
					return {
						...block,
						...(action.content !== undefined && { content: action.content }),
						...(action.question !== undefined && { question: action.question }),
						...(action.expectedAnswer !== undefined && { expectedAnswer: action.expectedAnswer }),
						...(action.possibleAnswers !== undefined && {
							possibleAnswers: action.possibleAnswers
						}),
						...(action.correctAnswers !== undefined && {
							correctAnswers: action.correctAnswers
						})
					};
				}
				return block;
			});
		}

		case ActionType.Enum.UPDATE_BLOCK_NAME: {
			return blocks.map((block) => {
				if (block.uuid === action.blockId) {
					return {
						...block,
						name: action.newName
					};
				}
				return block;
			});
		}

		default:
			throw new Error(`Unknown action type`);
	}

	return blocks;
}

export function queueBlockAction(action: BlockActionWithQuickAdd | UpdateBlockAction) {
	let parsedAction: BlockAction;

	if (action.type === ActionType.Enum.ADD_BLOCK) {
		if (action.data.type === BlockType.Enum.THEORY) {
			parsedAction = {
				type: ActionType.Enum.ADD_BLOCK,
				index: action.index,
				blockId: getTempId(),
				data: {
					type: action.data.type,
					name: action.data.name,
					position: action.data.position,
					content: action.data.content || ''
				},
				language: action.language
			};
		} else if (action.data.type === BlockType.Enum.MULTIPLE_CHOICE) {
			parsedAction = {
				type: ActionType.Enum.ADD_BLOCK,
				index: action.index,
				blockId: getTempId(),
				data: {
					type: action.data.type,
					name: action.data.name,
					position: action.data.position,
					question: action.data.question || 'placeholder question',
					possibleAnswers: action.data.possibleAnswers || [],
					correctAnswers: action.data.correctAnswers || []
				},
				language: action.language
			};
		} else if (action.data.type === BlockType.Enum.QUESTION) {
			parsedAction = {
				type: ActionType.Enum.ADD_BLOCK,
				index: action.index,
				blockId: getTempId(),
				data: {
					type: action.data.type,
					name: action.data.name,
					position: action.data.position,
					question: action.data.question || 'placeholder question',
					expectedAnswer: action.data.expectedAnswer || 'placeholder answer'
				},
				language: action.language
			};
		} else {
			throw new Error('Unsupported block type.');
		}
	} else if (action.type === ActionType.Enum.REORDER_BLOCK) {
		parsedAction = {
			type: ActionType.Enum.REORDER_BLOCK,
			blockId: action.blockId,
			newIndex: action.newIndex
		};
	} else if (action.type === ActionType.Enum.UPDATE_BLOCK) {
		parsedAction = {
			type: ActionType.Enum.UPDATE_BLOCK,
			blockId: action.blockId,
			...(action.content !== undefined && { content: action.content }),
			...(action.question !== undefined && { question: action.question }),
			...(action.expectedAnswer !== undefined && { expectedAnswer: action.expectedAnswer }),
			...(action.possibleAnswers !== undefined && { possibleAnswers: action.possibleAnswers }),
			...(action.correctAnswers !== undefined && { correctAnswers: action.correctAnswers }),
			language: action.language
		};
	} else if (action.type === ActionType.Enum.UPDATE_BLOCK_NAME) {
		parsedAction = {
			type: ActionType.Enum.UPDATE_BLOCK_NAME,
			blockId: action.blockId,
			newName: action.newName,
			language: action.language
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
