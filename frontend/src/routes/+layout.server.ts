import { loadUserInfo, isLoggedIn } from '$lib/server/auth';

export const load = async () => {
	if (isLoggedIn()) {
		const userInfo = await loadUserInfo();
		return {
			userInfo
		};
	}

	return {
		userInfo: null
	};
};
