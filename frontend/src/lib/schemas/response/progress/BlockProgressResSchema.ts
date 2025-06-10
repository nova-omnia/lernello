import { z } from 'zod';

export const TheoryBlockProgressResSchema = z.object({
	blockId: z.string().uuid(),
	isViewed: z.boolean(),
	blockType: z.literal('THEORY')
});

export type TheoryBlockProgressRes = z.infer<typeof TheoryBlockProgressResSchema>;

export const QuestionBlockProgressResSchema = z.object({
	blockId: z.string().uuid(),
	lastAnswer: z.string().nullable(),
	isCorrect: z.boolean().nullable(),
	blockType: z.literal('QUESTION')
});

export type QuestionBlockProgressRes = z.infer<typeof QuestionBlockProgressResSchema>;

export const MultipleChoiceBlockProgressResSchema = z.object({
	blockId: z.string().uuid(),
	lastAnswers: z.array(z.string()).nullable(),
	isCorrect: z.boolean().nullable(),
	blockType: z.literal('MULTIPLE_CHOICE')
});

export type MultipleChoiceBlockProgressRes = z.infer<typeof MultipleChoiceBlockProgressResSchema>;

export const BlockProgressResSchema = z.discriminatedUnion('blockType', [
	TheoryBlockProgressResSchema,
	MultipleChoiceBlockProgressResSchema,
	QuestionBlockProgressResSchema
]);

export type BlockProgressRes = z.infer<typeof BlockProgressResSchema>;
