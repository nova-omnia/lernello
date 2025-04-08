import { z } from 'zod';

export const LoggedInUserSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	token: z.string().nonempty(),
	changedPassword: z.boolean(),
	expires: z.string().datetime({
		offset: true
	})
});
export type LoggedInUser = z.infer<typeof LoggedInUserSchema>;
