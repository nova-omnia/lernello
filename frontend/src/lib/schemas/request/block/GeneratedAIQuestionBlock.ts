import { z } from 'zod';

export const GeneratedAIQuestionBlockSchema = z.object({
    blockId: z.string().nonempty(),
    multipleChoiceBlockId: z.string().nonempty()
});
export type GeneratedAIQuestionBlock = z.infer<typeof GeneratedAIQuestionBlockSchema>;