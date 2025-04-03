import type { BlockRes } from '$lib/schemas/response/BlockRes';

export const load = async () => {
	return {
		blocks: [
			{ uuid: 'a1', name: 'Block 1', type: 'theory' },
			{ uuid: 'b2', name: 'Block 2', type: 'quiz' },
			{ uuid: 'c3', name: 'Block 3', type: 'theory' },
			{ uuid: 'd4', name: 'Block 4', type: 'quiz' }
		] as BlockRes[]
	};
};
