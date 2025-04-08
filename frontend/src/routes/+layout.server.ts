import { loadUserInfo, recoverSession } from '$lib/server/auth';

export const load = async () => {
	const tokenInfo = recoverSession();

	if (!tokenInfo) {
		return { userInfo: null };
	}
	const userInfo = await loadUserInfo();
	return { userInfo: userInfo?.locale };
};
