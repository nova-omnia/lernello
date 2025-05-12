import type { LearningUnitProgressRes } from '$lib/schemas/response/progress/LearningUnitProgressResSchema';
import type { BlockProgressRes } from '$lib/schemas/response/progress/BlockProgressResSchema';

let currentProgress = $state<LearningUnitProgressRes | null>(null);
const blockProgressMap = $derived.by(() => {
	const map = new Map<string, BlockProgressRes>();
	if (currentProgress?.userBlockProgresses) {
		for (const bp of currentProgress.userBlockProgresses) {
			map.set(bp.blockId, bp);
		}
	}
	return map;
});

export const learningUnitProgressState = {
	get progress() {
		return currentProgress;
	},
	setProgress(progress: LearningUnitProgressRes | null) {
		currentProgress = progress;
	},
	getBlockProgress(blockId: string): BlockProgressRes | undefined {
		return blockProgressMap.get(blockId);
	},
	clearProgress() {
		currentProgress = null;
	}
};
