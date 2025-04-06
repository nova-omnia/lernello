import type { BlockRes, BlockResType } from '$lib/schemas/response/BlockRes';

let currTempId = 0;
function getTempId() {
	// Generate a temporary ID for new blocks
	const tempId = `tempid:${currTempId}`;
	currTempId++;
	return tempId;
}

type BlockAction =
	| {
			type: 'REORDER_BLOCK';
			data: {
				blockId: string;
				newIndex: number;
			};
	  }
	| {
			type: 'REMOVE_BLOCK';
			data: {
				blockId: string;
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
		case 'ADD_BLOCK':
			if (action.data.index !== undefined) {
				blocks.splice(action.data.index, 0, { ...action.data, uuid: getTempId() });
			} else {
				blocks.push({ ...action.data, uuid: getTempId() });
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
		case 'REMOVE_BLOCK': {
			blocks = blocks.filter((block) => block.uuid !== action.data.blockId);
			break;
		}
		default:
			throw new Error('Unknown action type');
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
