import { z } from 'zod';

export const LearningKitProgressResSchema = z.object({
	learningKitId: z.string().uuid(),
	userId: z.string().uuid(),
	isOpened: z.boolean(),
	isCompleted: z.boolean(),
	firstOpenedAt: z.string().datetime({ offset: true }).nullable(),
	lastOpenedAt: z.string().datetime({ offset: true }).nullable(),
	completedAt: z.string().datetime({ offset: true }).nullable(),
	progressPercentage: z.number().int().min(0).max(100)
});

export type LearningKitProgressRes = z.infer<typeof LearningKitProgressResSchema>;
