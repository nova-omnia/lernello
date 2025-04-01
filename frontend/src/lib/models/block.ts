import { z } from 'zod';

export const BlockTypeSchema = z.enum(['theory', 'quiz']);
export type BlockType = z.infer<typeof BlockTypeSchema>;

export const BlockSchema = z.object({
	uuid: z.union([z.string().uuid(), z.symbol()]),
	name: z.string(),
	type: BlockTypeSchema
});
export type Block = z.infer<typeof BlockSchema>;
