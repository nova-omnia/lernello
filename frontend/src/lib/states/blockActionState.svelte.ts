import type { BlockRes, BlockResType } from '$lib/schemas/response/BlockRes';

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
				type: BlockResType;
				index?: number; // Optional index for adding at a specific position vs just append
				name: string;
			};
	  };

type BlockActionState = {
	queue: BlockAction[];
	blocks: BlockRes[];
};

export const blockActionState: BlockActionState = $state({
	queue: [],
	blocks: []
});

function applyBlockAction(action: BlockAction, blocks: BlockRes[]): BlockRes[] {
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

export function queueBlockAction(action: BlockAction) {
	blockActionState.blocks = applyBlockAction(action, blockActionState.blocks);
	blockActionState.queue.push(action);
}
