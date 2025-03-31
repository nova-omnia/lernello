import { type UserLogin, type UserToken, UserTokenSchema } from '$lib/models/user';
import { request } from '$lib/api/apiClient';

export async function login(user: UserLogin): Promise<UserToken> {
	const loginUserRes = await request(`/api/auth/signin`, 'POST', {
		body: JSON.stringify(user)
	});
	return UserTokenSchema.parse(loginUserRes);
}
