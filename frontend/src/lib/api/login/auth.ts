import { request } from '$lib/api/apiClient';
import type { UserLogin } from '$lib/schemas/request/UserLogin';
import { LoggedInUserSchema, type LoggedInUser } from '$lib/schemas/response/LoggedInUser';

export async function login(user: UserLogin): Promise<LoggedInUser> {
	const loginUserRes = await request(`/api/auth/signin`, 'POST', {
		body: JSON.stringify(user)
	});
	return LoggedInUserSchema.parse(loginUserRes);
}
