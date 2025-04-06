import { z } from 'zod';

export const LoggedInUserSchema = z.object({
	token: z.string().nonempty(),
	expires: z.number().int().nonnegative()
});
export type LoggedInUser = z.infer<typeof LoggedInUserSchema>;
