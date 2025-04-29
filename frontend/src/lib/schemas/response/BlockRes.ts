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

const TheoryBlockResSchema = BaseBlockResSchema.extend({
	type: z.literal(THEORY_BLOCK_TYPE),
	content: z.string()
});

const MultipleChoiceBlockResSchema = BaseBlockResSchema.extend({
	type: z.literal(MULTIPLE_CHOICE_BLOCK_TYPE),
	question: z.string(),
	possibleAnswers: z.array(z.string()),
	correctAnswers: z.array(z.string())
});

const QuestionBlockResSchema = BaseBlockResSchema.extend({
	type: z.literal(QUESTION_BLOCK_TYPE),
	question: z.string(),
	expectedAnswer: z.string()
});
const MultimediaBlockResSchema = BaseBlockResSchema.extend({
	type: z.literal('MULTIMEDIA')
});

export const BlockResSchema = z.discriminatedUnion('type', [
	TheoryBlockResSchema,
	MultipleChoiceBlockResSchema,
	QuestionBlockResSchema,
	MultimediaBlockResSchema
]);

export type BlockRes = z.infer<typeof BlockResSchema>;
