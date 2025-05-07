import { z } from 'zod';

export const CheckQuestionAnswerSchema = z.object({
    blockId: z.string().uuid(),
    answer: z.string()
});

export type CheckQuestionAnswer = z.infer<typeof CheckQuestionAnswerSchema>;