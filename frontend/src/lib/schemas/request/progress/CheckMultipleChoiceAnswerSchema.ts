import { z } from 'zod';

export const CheckMultipleChoiceAnswerSchema = z.object({
    blockId: z.string().uuid(),
    answers: z.array(z.string())
});

export type CheckMultipleChoiceAnswer = z.infer<typeof CheckMultipleChoiceAnswerSchema>;

