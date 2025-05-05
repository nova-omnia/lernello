import { z } from 'zod';

export const StaticFileResSchema = z.object({
	fileUrl: z.string().url()
});

export type StaticFileRes = z.infer<typeof StaticFileResSchema>;
