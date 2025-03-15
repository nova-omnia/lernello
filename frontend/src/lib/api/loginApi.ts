import type { LoginUser } from '$lib/models/LoginUser';
import { request } from '$lib/apiClient';

export async function requestLoginUser(user: LoginUser): Promise<LoginUser> {
	return request<LoginUser>(`/login`, 'POST', user);
}
