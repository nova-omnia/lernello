import { z } from 'zod';

export const BlockTypeSchema = z.enum(['theory', 'quiz']); //Adjust based on your block types

const BaseBlockActionSchema = z.object({
	blockId: z.string().min(1) // Changed from uuid() to allow temp IDs
});

export const AddBlockActionSchema = BaseBlockActionSchema.extend({
	type: z.literal('ADD_BLOCK'),
	data: z.object({
		type: BlockTypeSchema,
		index: z.number().min(0).optional(),
		name: z.string().min(1),
		blockPayload: z.any().optional() // Made optional for frontend
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

export type BlockAction = z.infer<typeof BlockActionSchema>;
export type AddBlockAction = z.infer<typeof AddBlockActionSchema>;
export type ReorderBlockAction = z.infer<typeof ReorderBlockActionSchema>;
export type RemoveBlockAction = z.infer<typeof RemoveBlockActionSchema>;