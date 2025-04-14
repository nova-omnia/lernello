import { z } from 'zod';

export const BlockResTypeSchema = z.enum(['THEORY', 'MULTIPLE_CHOICE', 'QUESTION']);
export type BlockResType = z.infer<typeof BlockResTypeSchema>;

const BaseBlockResSchema = z.object({
	uuid: z.union([z.string().uuid(), z.string().startsWith('tempid:')]),
	name: z.string(),
	type: BlockResTypeSchema,
	position: z.number().min(0)
});

const TheoryBlockResSchema = BaseBlockResSchema.extend({
	type: z.literal('THEORY'),
	content: z.string()
});

const MultipleChoiceBlockResSchema = BaseBlockResSchema.extend({
	type: z.literal('MULTIPLE_CHOICE'),
	question: z.string(),
	possibleAnswers: z.array(z.string()),
	correctAnswers: z.array(z.string())
});

const QuestionBlockResSchema = BaseBlockResSchema.extend({
	type: z.literal('QUESTION'),
	question: z.string(),
	expectedAnswer: z.string()
});

export const BlockResSchema = z.discriminatedUnion('type', [
	TheoryBlockResSchema,
	MultipleChoiceBlockResSchema,
	QuestionBlockResSchema
]);

export type BlockRes = z.infer<typeof BlockResSchema>;
