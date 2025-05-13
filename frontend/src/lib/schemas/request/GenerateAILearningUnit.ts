import { z } from 'zod';

export const GenerateAILearningUnitSchema = z.object({
	fileIds: z.array(z.string().uuid())
});
