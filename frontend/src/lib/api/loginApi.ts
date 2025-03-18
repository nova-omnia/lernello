import { UserTokenSchema, type LoginUser, type UserToken } from '$lib/models/LoginUser';
import { request } from '$lib/api/apiClient';

export async function requestLoginUser(user: LoginUser): Promise<UserToken> {
	const loginUserRes = await request(`/api/auth/signin`, 'POST', {
		body: JSON.stringify(user)
	});
	const loginUser = UserTokenSchema.parse(loginUserRes);
	return loginUser;
}
