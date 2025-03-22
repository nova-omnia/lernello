import { fail } from '@sveltejs/kit';
import { UserTokenSchema } from '$lib/models/user';

export function getUsernameFromCookie(cookies) {
	const tokenData = cookies.get('sessionToken');
	if (!tokenData) {
		return fail(401);
	}

	let parsed;
	try {
		parsed = UserTokenSchema.parse(JSON.parse(tokenData));
	} catch (err) {
		return fail(400, err);
	}

	return parsed.username;
}
