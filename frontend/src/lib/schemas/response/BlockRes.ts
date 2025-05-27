import { z } from 'zod';

export const THEORY_BLOCK_TYPE = 'THEORY' as const;
export const MULTIPLE_CHOICE_BLOCK_TYPE = 'MULTIPLE_CHOICE' as const;
export const QUESTION_BLOCK_TYPE = 'QUESTION' as const;
export const BlockResTypeSchema = z.enum([
	THEORY_BLOCK_TYPE,
	MULTIPLE_CHOICE_BLOCK_TYPE,
	QUESTION_BLOCK_TYPE
]);
export type BlockResType = z.infer<typeof BlockResTypeSchema>;

const BaseBlockResSchema = z.object({
	uuid: z.union([z.string().uuid(), z.string().startsWith('tempid:')]),
	name: z.string(),
	type: BlockResTypeSchema,
	position: z.number().min(0)
});

export const TranslatedBlockResSchema = z.object({
	id: z.string().uuid(),
	name: z.string(),
	language: z.enum(['GERMAN', 'FRENCH', 'ITALIAN']),
	content: z.string().nullable(),
	question: z.string().nullable(),
	expectedAnswer: z.string().nullable(),
	possibleAnswers: z.array(z.string()).nullable(),
	correctAnswers: z.array(z.string()).nullable()
});

export const TheoryBlockResSchema = BaseBlockResSchema.extend({
	type: z.literal(THEORY_BLOCK_TYPE),
	content: z.string(),
	translatedContents: z.array(TranslatedBlockResSchema).nullable().optional()
});

export const MultipleChoiceBlockResSchema = BaseBlockResSchema.extend({
	type: z.literal(MULTIPLE_CHOICE_BLOCK_TYPE),
	question: z.string(),
	possibleAnswers: z.array(z.string()),
	correctAnswers: z.array(z.string()),
	translatedContents: z.array(TranslatedBlockResSchema).nullable().optional()
});

export const QuestionBlockResSchema = BaseBlockResSchema.extend({
	type: z.literal(QUESTION_BLOCK_TYPE),
	question: z.string(),
	expectedAnswer: z.string(),
	translatedContents: z.array(TranslatedBlockResSchema).nullable().optional()
});

export const BlockResSchema = z.discriminatedUnion('type', [
	TheoryBlockResSchema,
	MultipleChoiceBlockResSchema,
	QuestionBlockResSchema
]);

export type BlockRes = z.infer<typeof BlockResSchema>;
