import { z } from 'zod';

export const LoggedInUserSchema = z.object({
	token: z.string().nonempty(),
	expires: z.string().datetime({
		offset: true
	})
});
export type LoggedInUser = z.infer<typeof LoggedInUserSchema>;
