import { z } from 'zod';

export const ChangePasswordDataSchema = z.object({
	oldPassword: z.string().min(8).nonempty(),
	newPassword: z.string().min(8).nonempty(),
	confirmPassword: z.string().min(8).nonempty()
});
export const ChangePasswordData = z.infer<typeof ChangePasswordDataSchema>;

export class ChangedPasswordUser {
	uuid: string;
	oldPassword: string;
	newPassword: string;
	confirmPassword: string;

	constructor(uuid: string, oldPassword: string, newPassword: string, confirmPassword: string) {
		this.uuid = uuid;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}
}

export const ChangePasswordStatusSchema = z.object({
	success: z.boolean()
});
export const ChangePasswordStatus = z.infer<typeof ChangePasswordStatusSchema>;
