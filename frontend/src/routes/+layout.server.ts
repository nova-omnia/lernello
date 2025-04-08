import { loadUserInfo, recoverAuthToken } from '$lib/server/auth';

export const load = async () => {
	const tokenInfo = recoverAuthToken();

	if (tokenInfo) {
		const userInfo = await loadUserInfo();
		return {
			userInfo
		};
	}

	return {
		userInfo: null
	};
};
