import { z } from 'zod';

export const TheoryBlockViewedResSchema = z.object({
	blockId: z.string().uuid()
});

export type TheoryBlockViewed = z.infer<typeof TheoryBlockViewedResSchema>;
