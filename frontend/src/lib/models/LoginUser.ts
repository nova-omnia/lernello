import { z } from 'zod';

export const LoginUserSchema = z.object({
	email: z.string().email().nonempty(),
	password: z.string().min(8).nonempty()
});

export type LoginUser = z.infer<typeof LoginUserSchema>;

export function parseLoginUser(data: FormData): LoginUser {
	const rawData = {
		email: data.get('email'),
		password: data.get('password')
	};

	const result = LoginUserSchema.safeParse(rawData);
	if (!result.success) {
		throw new Error('Invalide login-data: ' + JSON.stringify(result.error.format()));
	}
	return result.data;
}
