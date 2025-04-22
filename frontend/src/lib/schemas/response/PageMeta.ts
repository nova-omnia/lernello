import { z } from 'zod';

export const PageMetaSchema = z.object({
	pageable: z.any(),
	totalPages: z.number(),
	totalElements: z.number(),
	last: z.boolean(),
	size: z.number(),
	number: z.number(),
	sort: z.any(),
	numberOfElements: z.number(),
	first: z.boolean(),
	empty: z.boolean()
});
