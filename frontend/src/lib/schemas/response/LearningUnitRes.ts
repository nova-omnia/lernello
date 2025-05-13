import z from 'zod';
import { BlockResSchema } from '$lib/schemas/response/BlockRes';

export const LearningUnitResSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	name: z.string().nonempty(),
	blocks: z.array(BlockResSchema),
	position: z.number().int().nonnegative()
});
export type LearningUnitRes = z.infer<typeof LearningUnitResSchema>;
