import type { Block, BlockType } from '$lib/models/block';

type BlockAction =
	| {
			type: 'REORDER_BLOCK';
			data: {
				blockId: string | symbol;
				newIndex: number;
			};
	  }
	| {
			type: 'ADD_BLOCK';
			data: {
				type: BlockType;
				index?: number; // Optional index for adding at a specific position vs just append
				name: string;
			};
	  };

type BlockActionQueue = {
	actions: BlockAction[];
	blocks: Block[];
};

export const learningUnitActionQueue: BlockActionQueue = $state({
	actions: [],
	blocks: []
});

function applyLearningUnitAction(action: BlockAction, blocks: Block[]): Block[] {
	switch (action.type) {
		case 'ADD_BLOCK':
			if (action.data.index !== undefined) {
				blocks.splice(action.data.index, 0, { ...action.data, uuid: Symbol() });
			} else {
				blocks.push({ ...action.data, uuid: Symbol() });
			}
			break;
		case 'REORDER_BLOCK': {
			const blockToMove = blocks.find((block) => block.uuid === action.data.blockId);

			if (blockToMove) {
				blocks = blocks.filter((block) => block.uuid !== action.data.blockId);
				blocks.splice(action.data.newIndex, 0, blockToMove);
			}
			break;
		}
		default:
			throw new Error('Unknown action type');
	}

	return blocks;
}

export function queueLearningUnitAction(action: BlockAction) {
	learningUnitActionQueue.blocks = applyLearningUnitAction(action, learningUnitActionQueue.blocks);
	learningUnitActionQueue.actions.push(action);
}
