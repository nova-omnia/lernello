import { z } from 'zod';

export const BlockType = z.enum(['THEORY', 'MULTIPLE_CHOICE']);

export const UpdateTheoryBlockSchema = z.object({
	type: z.literal(BlockType.Enum.THEORY),
	name: z.string().min(3).max(40),
	learningUnitId: z.string().uuid().nullable().optional(),
	content: z.string().min(1).nullable().optional()
});

export const UpdateMultipleChoiceBlockSchema = z.object({
	type: z.literal(BlockType.Enum.MULTIPLE_CHOICE),
	name: z.string().min(3).max(40),
	learningUnitId: z.string().uuid().nullable().optional(),
	question: z.string().min(1),
	possibleAnswers: z.array(z.string().min(1)),
	correctAnswers: z.array(z.string().min(1))
});
