import { z } from 'zod';

export const RoleType = z.enum(['INSTRUCTOR', 'TRAINEE']);

export const UserRoleSchema = z.object({
	type: RoleType
});
export type UserRole = z.infer<typeof UserRoleSchema>;
