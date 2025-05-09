import { json } from '@sveltejs/kit';
import type { RequestHandler } from './$types';
import { recoverSession } from '$lib/server/auth';

export const GET: RequestHandler = async () => {
	const loggedInUserNoRefresh = recoverSession();

	return json(loggedInUserNoRefresh);
};
