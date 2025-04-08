import { redirect } from '@sveltejs/kit';

export async function POST({ cookies }) {
	cookies.delete('sessionToken', { path: '/' });
	throw redirect(303, '/login'); // Beispiel: Weiterleitung nach dem Logout
}
