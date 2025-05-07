import { z } from 'zod';

export const QuestionAnswerValidationResSchema = z.object({
	blockId: z.string().uuid(),
	isCorrect: z.boolean()
});

export type QuestionAnswerValidationRes = z.infer<typeof QuestionAnswerValidationResSchema>;
