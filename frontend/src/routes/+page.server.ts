import {requestLoginUser} from "$lib/api/loginApi";
import type { Actions } from './$types';
import {LoginUser} from "$lib/models/LoginUser";

export const actions = {
    loginUser: async ({ request }) => {
        const data = await request.formData();
        const user = new LoginUser(data);

        return requestLoginUser(user);
	}
} satisfies Actions;
