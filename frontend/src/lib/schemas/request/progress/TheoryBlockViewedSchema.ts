import { z } from 'zod';

export const TheoryBlockViewedSchema = z.object({
	blockId: z.string().uuid()
});

export type TheoryBlockViewed = z.infer<typeof TheoryBlockViewedSchema>;
