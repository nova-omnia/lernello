import { z } from 'zod';

export const MultipleChoiceAnswerValidationResSchema = z.object({
    blockId: z.string().uuid(),
    isCorrect: z.boolean()
});

export type MultipleChoiceAnswerValidationRes = z.infer<typeof MultipleChoiceAnswerValidationResSchema>;