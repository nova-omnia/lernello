import { z } from 'zod';

export const UserSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	username: z.string().email().nonempty(),
	name: z.string().nonempty(),
	surname: z.string().nonempty()
});
export type User = z.infer<typeof UserSchema>;
