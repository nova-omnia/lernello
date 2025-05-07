import { z } from 'zod';

export const LearningKitOpenedSchema = z.object({
	learningKitId: z.string().uuid()
});

export type LearningKitOpened = z.infer<typeof LearningKitOpenedSchema>;
