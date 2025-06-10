import { z } from 'zod';

export const GenerateAILearningUnitSchema = z.object({
	fileIds: z.array(z.string().uuid()),
	prompt: z.string().optional(),
	difficulty: z.string().optional(),
	includeTheory: z.boolean(),
	includeQuestions: z.boolean(),
	includeMultipleChoice: z.boolean()
});
