import { z } from 'zod';
import { Role } from '$lib/schemas/response/UserInfo';

export const UserResSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	username: z.string().email().nonempty(),
	name: z.string().nonempty(),
	surname: z.string().nonempty(),
	role: Role
});
export type UserRes = z.infer<typeof UserResSchema>;
