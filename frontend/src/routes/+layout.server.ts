import { loadUserInfo, recoverSession } from '$lib/server/auth';

export const load = async () => {
	const tokenInfo = recoverSession();

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
