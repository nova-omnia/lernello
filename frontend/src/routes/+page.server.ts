import { requireLogin } from '$lib/server/auth';
import { redirect } from '@sveltejs/kit';

export const load = async () => {
	requireLogin();
	redirect(307, '/dashboard');
};
