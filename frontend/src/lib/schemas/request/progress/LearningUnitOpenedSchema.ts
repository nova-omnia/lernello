import { z } from 'zod';

export const LearningUnitOpenedSchema = z.object({
	learningUnitId: z.string().uuid()
});

export type LearningUnitOpened = z.infer<typeof LearningUnitOpenedSchema>;
