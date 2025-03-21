import { z } from 'zod';

export const ChangePasswordDataSchema = z.object({
	oldPassword: z.string().min(8).nonempty(),
	newPassword: z.string().min(8).nonempty(),
	confirmPassword: z.string().min(8).nonempty()
});
export const ChangePasswordData = z.infer<typeof ChangePasswordDataSchema>;

export class ChangedPasswordUser {
	username: string;
	oldPassword: string;
	newPassword: string;
	confirmPassword: string;

	constructor(username: string, oldPassword: string, newPassword: string, confirmPassword: string) {
		this.username = username;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}
}

export const ChangePasswordStatusSchema = z.object({
	success: z.boolean()
});
export const ChangePasswordStatus = z.infer<typeof ChangePasswordStatusSchema>;
