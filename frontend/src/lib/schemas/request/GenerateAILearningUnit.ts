import { z } from 'zod';

export const GenerateAILearningUnitSchema = z.object({
	files: z.array(z.string().uuid())
});
