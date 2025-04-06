import { z } from 'zod';

const CreateMultipleChoiceBlockDTOSchema = z.object({
	name: z.string().min(1),
	position: z.number().min(0),
	learningUnitId: z.string().uuid(),
	question: z.string().min(1),
	possibleAnswers: z.array(z.string().min(1)),
	correctAnswers: z.array(z.string().min(1))
});

const CreateQuestionBlockDTOSchema = z.object({
	name: z.string().min(1),
	position: z.number().min(0),
	learningUnitId: z.string().uuid(),
	question: z.string().min(1),
	expectedAnswer: z.string().min(1)
});

const CreateTheoryBlockDTOSchema = z.object({
	name: z.string().min(1),
	position: z.number().min(0),
	learningUnitId: z.string().uuid(),
	content: z.string().min(1)
});

export const CreateBlockSchema = z.union([
	CreateMultipleChoiceBlockDTOSchema,
	CreateQuestionBlockDTOSchema,
	CreateTheoryBlockDTOSchema
]);
