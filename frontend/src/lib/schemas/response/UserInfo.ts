import { z } from 'zod';

export const Role = z.enum(['INSTRUCTOR', 'TRAINEE']);

export const UserInfoSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	username: z.string().nonempty(),
	locale: z.string().nullable(),
	changedPassword: z.boolean(),
	role: Role
});
export type UserInfo = z.infer<typeof UserInfoSchema>;
