import { z } from 'zod';

export const BlockResTypeSchema = z.enum(['THEORY', 'MULTIPLE_CHOICE']);
export type BlockResType = z.infer<typeof BlockResTypeSchema>;

export const BlockResSchema = z.object({
	uuid: z.union([z.string().uuid(), z.string().startsWith('tempid:')]),
	name: z.string(),
	type: BlockResTypeSchema
});
export type BlockRes = z.infer<typeof BlockResSchema>;
