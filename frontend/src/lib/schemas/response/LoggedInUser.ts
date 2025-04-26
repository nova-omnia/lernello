import { z } from 'zod';
import { Role } from '$lib/schemas/response/UserInfo';

export const LoggedInUserSchema = z.object({
	token: z.string().nonempty(),
	expires: z.string().datetime({
		offset: true
	}),
	role: Role
});
export type LoggedInUser = z.infer<typeof LoggedInUserSchema>;
