import { z } from 'zod';

export const ChangePasswordDataSchema = z
	.object({
		newPassword: z.string().min(8).nonempty(),
		confirmPassword: z.string().min(8).nonempty()
	})
	.refine((data) => data.newPassword === data.confirmPassword, {
		message: 'New password and confirm password must match',
		path: ['confirmPassword']
	});
export type ChangePasswordData = z.infer<typeof ChangePasswordDataSchema>;
