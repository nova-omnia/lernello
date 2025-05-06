import { z } from 'zod';
import {
	CreateTheoryBlockSchema,
	CreateMultipleChoiceBlockSchema,
	CreateQuestionBlockSchema
} from './CreateBlock';

export const ActionType = z.enum(['ADD_BLOCK', 'REORDER_BLOCK', 'REMOVE_BLOCK', 'UPDATE_BLOCK', 'UPDATE_BLOCK_NAME']);

const BaseBlockActionSchema = z.object({
	blockId: z.string(),
});

export const AddBlockActionSchema = BaseBlockActionSchema.extend({
	type: z.literal(ActionType.Enum.ADD_BLOCK),
	index: z.number().min(-1),
	data: z.discriminatedUnion('type', [
		CreateTheoryBlockSchema,
		CreateMultipleChoiceBlockSchema,
		CreateQuestionBlockSchema
	])
});

export const ReorderBlockActionSchema = BaseBlockActionSchema.extend({
	type: z.literal('REORDER_BLOCK'),
	newIndex: z.number().min(-1)
});

export const RemoveBlockActionSchema = BaseBlockActionSchema.extend({
	type: z.literal('REMOVE_BLOCK')
});

export const UpdateBlockActionSchema = BaseBlockActionSchema.extend({
	type: z.literal(ActionType.Enum.UPDATE_BLOCK),
	content: z.string().optional(),
	question: z.string().optional(),
	expectedAnswer: z.string().optional(),
	possibleAnswers: z.array(z.string()).optional(),
	correctAnswers: z.array(z.string()).optional()
});

export const UpdateBlockNameActionSchema = BaseBlockActionSchema.extend({
	type: z.literal(ActionType.Enum.UPDATE_BLOCK_NAME),
	newName: z.string().min(3).max(40)
});

export const BlockActionSchema = z.discriminatedUnion('type', [
	AddBlockActionSchema,
	ReorderBlockActionSchema,
	RemoveBlockActionSchema,
	UpdateBlockActionSchema,
	UpdateBlockNameActionSchema
]);

type AddBlockActionNoId = Omit<z.infer<typeof AddBlockActionSchema>, 'blockId'>;

export type BlockAction = z.infer<typeof BlockActionSchema>;
export type BlockActionWithQuickAdd = AddBlockActionNoId | BlockAction;
export type AddBlockAction = z.infer<typeof AddBlockActionSchema>;
export type ReorderBlockAction = z.infer<typeof ReorderBlockActionSchema>;
export type RemoveBlockAction = z.infer<typeof RemoveBlockActionSchema>;
export type UpdateBlockAction = z.infer<typeof UpdateBlockActionSchema>;
