import { z } from 'zod';

export const LoggedInUserSchema = z.object({
	token: z.string().nonempty(),
	refreshToken: z.string().nonempty(),
	expires: z.string().datetime({
		offset: true
	}),
	refreshExpires: z.string().datetime({
		offset: true
	})
});
export type LoggedInUser = z.infer<typeof LoggedInUserSchema>;
