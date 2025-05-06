import { z } from 'zod';
import { Role } from '$lib/schemas/response/UserInfo';

export const UpdateUserSchema = z.object({
	username: z.string().email().optional(),
	name: z
		.string()
		.min(3, { message: 'Name must be at least 3 characters' })
		.max(40, { message: 'Name cannot exceed 40 characters' })
		.optional(),
	surname: z
		.string()
		.min(3, { message: 'Name must be at least 3 characters' })
		.max(40, { message: 'Name cannot exceed 40 characters' })
		.optional(),
	role: Role.optional()
});

export type UpdateUser = z.infer<typeof UpdateUserSchema>;
