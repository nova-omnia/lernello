import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';
import type { LearningKitProgressRes } from '$lib/schemas/response/progress/LearningKitProgressResSchema';

export interface KitWithProgress extends LearningKitRes {
	participantProgress?: LearningKitProgressRes[];
	averageProgress?: number;
	completionRate?: number;
}
