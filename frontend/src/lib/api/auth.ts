import { UserTokenSchema, type UserLogin, type UserToken } from '$lib/models/user';
import { request } from '$lib/api/apiClient';

export async function login(user: UserLogin): Promise<UserToken> {
	const loginUserRes = await request(`/api/auth/signin`, 'POST', {
		body: JSON.stringify(user)
	});
	const loginUser = UserTokenSchema.parse(loginUserRes);
	return loginUser;
}
