import { redirect } from '@sveltejs/kit';

export async function POST({ cookies }) {
	cookies.delete('lernello_auth_token', { path: '/' });
	throw redirect(303, '/login'); // Beispiel: Weiterleitung nach dem Logout
}
