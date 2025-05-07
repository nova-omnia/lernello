import { z } from 'zod';

export const GeneratedAIQuestionBlockSchema = z.object({
	theoryBlockId: z.string().nonempty()
});
export type GeneratedAIQuestionBlock = z.infer<typeof GeneratedAIQuestionBlockSchema>;
