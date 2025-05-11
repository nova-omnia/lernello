import { z } from 'zod';

export const GenericSuccessSchema = z.object({
	success: z.literal(true)
});
export type GenericSuccess = z.infer<typeof GenericSuccessSchema>;
