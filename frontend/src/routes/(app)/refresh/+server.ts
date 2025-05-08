import { json } from '@sveltejs/kit';
import type { RequestHandler } from './$types';
import { api } from '$lib/api/apiClient';

export const GET: RequestHandler = async ({ fetch }) => {
	// const number = Math.floor(Math.random() * 6) + 1;
	// const newToken = await api(fetch).req('refresh', {}, undefined).response;

	return json(99);
};
