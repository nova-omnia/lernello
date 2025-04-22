import { z } from 'zod';

const SortSchema = z.object({
	empty: z.boolean(),
	sorted: z.boolean(),
	unsorted: z.boolean()
});

const PageableSchema = z.object({
	offset: z.number(),
	pageNumber: z.number(),
	pageSize: z.number(),
	paged: z.boolean(),
	unpaged: z.boolean(),
	sort: SortSchema
});

export const PageMetaSchema = z.object({
	pageable: PageableSchema,
	totalPages: z.number(),
	totalElements: z.number(),
	last: z.boolean(),
	size: z.number(),
	number: z.number(),
	sort: SortSchema,
	numberOfElements: z.number(),
	first: z.boolean(),
	empty: z.boolean()
});
