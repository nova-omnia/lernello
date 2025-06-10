import { z } from 'zod';
import { BlockProgressResSchema } from '$lib/schemas/response/progress/BlockProgressResSchema';

export const LearningUnitProgressResSchema = z.object({
	learningUnitId: z.string().uuid(),
	isOpened: z.boolean(),
	isCompleted: z.boolean(),
	firstOpenedAt: z.string().datetime({ offset: true }).nullable(),
	lastOpenedAt: z.string().datetime({ offset: true }).nullable(),
	completedAt: z.string().datetime({ offset: true }).nullable(),
	progressPercentage: z.number().int().min(0).max(100),
	userBlockProgresses: z.array(BlockProgressResSchema)
});

export type LearningUnitProgressRes = z.infer<typeof LearningUnitProgressResSchema>;
