import { z } from 'zod';
import { CreateTheoryBlockSchema, CreateMultipleChoiceBlockSchema } from './CreateBlock';

export const ActionType = z.enum(['ADD_BLOCK', 'REORDER_BLOCK', 'REMOVE_BLOCK']);

const BaseBlockActionSchema = z.object({
	blockId: z.string()
});

export const AddBlockActionSchema = BaseBlockActionSchema.extend({
	type: z.literal(ActionType.Enum.ADD_BLOCK),
	index: z.number().min(-1),
	data: z.discriminatedUnion('type', [CreateTheoryBlockSchema, CreateMultipleChoiceBlockSchema])
});

export const ReorderBlockActionSchema = BaseBlockActionSchema.extend({
	type: z.literal('REORDER_BLOCK'),
	newIndex: z.number().min(-1)
});

export const RemoveBlockActionSchema = BaseBlockActionSchema.extend({
	type: z.literal('REMOVE_BLOCK')
});

export const BlockActionSchema = z.discriminatedUnion('type', [
	AddBlockActionSchema,
	ReorderBlockActionSchema,
	RemoveBlockActionSchema
]);

type AddBlockActionNoId = Omit<z.infer<typeof AddBlockActionSchema>, 'blockId'>;

export type BlockAction = z.infer<typeof BlockActionSchema>;
export type BlockActionWithQuickAdd = AddBlockActionNoId | BlockAction;
export type AddBlockAction = z.infer<typeof AddBlockActionSchema>;
export type ReorderBlockAction = z.infer<typeof ReorderBlockActionSchema>;
export type RemoveBlockAction = z.infer<typeof RemoveBlockActionSchema>;
