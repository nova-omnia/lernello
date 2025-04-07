import { z } from 'zod';

export const UserLoginSchema = z.object({
	username: z.string().email().nonempty(),
	password: z.string().min(8).nonempty()
});
export type UserLogin = z.infer<typeof UserLoginSchema>;
