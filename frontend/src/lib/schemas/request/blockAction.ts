import { z } from 'zod';
import { CreateBlockSchema } from './CreateBlock';

export const BlockTypeSchema = z.enum(['theory', 'quiz']);

const BaseBlockActionSchema = z.object({
	blockId: z.string().min(1)
});

export const AddBlockActionSchema = BaseBlockActionSchema.extend({
	type: z.literal('ADD_BLOCK'),
	data: z.object({
		type: BlockTypeSchema,
		index: z.number().min(0).optional(),
		name: z.string().min(1),
		blockPayload: CreateBlockSchema.optional()
	})
});

export const ReorderBlockActionSchema = BaseBlockActionSchema.extend({
	type: z.literal('REORDER_BLOCK'),
	data: z.object({
		newIndex: z.number().min(0)
	})
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
