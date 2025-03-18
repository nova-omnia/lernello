import { z } from 'zod';

export const LoginUserSchema = z.object({
	username: z.string().email().nonempty(),
	password: z.string().min(8).nonempty()
});

export const UserTokenSchema = z.object({
	email: z.string().email().nonempty(),
	password: z.string().min(8).nonempty(),
	token: z.string().nonempty()
});

export type LoginUser = z.infer<typeof LoginUserSchema>;
export type UserToken = z.infer<typeof UserTokenSchema>;

export function parseLoginUser(data: FormData): LoginUser {
	const rawData = {
		username: data.get('username'),
		password: data.get('password')
	};

	const result = LoginUserSchema.safeParse(rawData);
	if (!result.success) {
		throw new Error('Invalide login-data: ' + JSON.stringify(result.error.format()));
	}
	return result.data;
}
