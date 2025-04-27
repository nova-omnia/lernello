import { z } from 'zod';

export const INSTRUCTOR_ROLE = 'INSTRUCTOR' as const;
export const TRAINEE_ROLE = 'TRAINEE' as const;
export const Role = z.enum([INSTRUCTOR_ROLE, TRAINEE_ROLE]);
export type RoleType = z.infer<typeof Role>;

export const UserInfoSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	username: z.string().nonempty(),
	locale: z.string().nullable(),
	changedPassword: z.boolean(),
	role: Role
});
export type UserInfo = z.infer<typeof UserInfoSchema>;
