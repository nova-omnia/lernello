import { requireLogin } from '$lib/server/auth';

export const load = async () => {
	requireLogin();
};
