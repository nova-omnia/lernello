import { z } from 'zod';

export const PasswordStatusSchema = z.object({
	success: z.boolean()
});
export type PasswordStatus = z.infer<typeof PasswordStatusSchema>;
