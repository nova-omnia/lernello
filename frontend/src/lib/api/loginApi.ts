import { request } from '../apiClient';
import type {LoginUser} from "$lib/models/LoginUser";

export async function requestLoginUser(user: LoginUser): Promise<LoginUser> {
    return request<LoginUser>(`/login`, 'POST', user);
}
