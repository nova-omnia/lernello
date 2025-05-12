import { z } from 'zod';

export const RenameLearningUnitSchema = z.object({
	name: z.string().min(3).max(40)
});
export type RenameLearningUnit = z.infer<typeof RenameLearningUnitSchema>;
