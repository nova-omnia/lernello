import { recoverSession } from '$lib/server/auth';
import { redirect } from '@sveltejs/kit';

export const load = async () => {
	const user = recoverSession();
	if (user) {
		redirect(303, '/');
	}
};
