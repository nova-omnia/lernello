import { z } from 'zod';

export const GeneratedAIQuestionBlockSchema = z.object({
	theoryBlockId: z.string().nonempty(),
	blockId: z.string().uuid()
});
export type GeneratedAIQuestionBlock = z.infer<typeof GeneratedAIQuestionBlockSchema>;
