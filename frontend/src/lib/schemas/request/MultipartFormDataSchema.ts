import { z } from 'zod';

export const MultipartFormDataSchema = z.object({
	file: z.instanceof(File).refine((file) => file.size > 0, {
		message: 'File must not be empty'
	}),
	metadata: z.optional(z.string())
});

export type MultipartFormData = z.infer<typeof MultipartFormDataSchema>;
