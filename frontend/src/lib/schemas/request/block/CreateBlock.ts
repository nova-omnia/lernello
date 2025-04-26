import { z } from 'zod';

export const BlockType = z.enum(['THEORY', 'MULTIPLE_CHOICE', 'QUESTION', 'MULTIMEDIA']);

export const CreateTheoryBlockSchema = z.object({
	type: z.literal(BlockType.Enum.THEORY),
	name: z.string().min(3).max(40),
	position: z.number().min(0),
	content: z.string().min(1).nullable().optional()
});

export const CreateMultipleChoiceBlockSchema = z.object({
	type: z.literal(BlockType.Enum.MULTIPLE_CHOICE),
	name: z.string().min(3).max(40),
	position: z.number().min(0),
	question: z.string().min(1),
	possibleAnswers: z.array(z.string().min(1)),
	correctAnswers: z.array(z.string().min(1))
});

export const CreateQuestionBlockSchema = z.object({
	type: z.literal(BlockType.Enum.QUESTION),
	name: z.string().min(3).max(40),
	position: z.number().min(0),
	question: z.string().min(1).optional(),
	expectedAnswer: z.string().min(1).nullable().optional()
});
