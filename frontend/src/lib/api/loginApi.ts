import type { LoginUser } from '$lib/models/LoginUser';
import { request } from '$lib/api/apiClient';

export async function requestLoginUser(user: LoginUser): Promise<LoginUser> {
	return request<LoginUser>('/api/auth/signin', 'POST', {
		body: JSON.stringify(user)
	});
}
