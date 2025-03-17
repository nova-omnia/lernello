import { ApiError, isApiErrorResponse } from './ApiError';

//TODO change BASE_URL
const BASE_URL = 'https://api.example.com';

type AllowedMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';

export async function request<T>(
	endpoint: string,
	method: AllowedMethod = 'GET',
	request?: RequestInit
): Promise<T> {
	const url = new URL(endpoint, BASE_URL).toString();
	try {
		const response = await fetch(url, {
			method: method,
			headers: {
				'Content-Type': 'application/json',
				...request?.headers
			},
			...request
		});
		if (!response.ok) {
			const errorData = await response.json();
			if (isApiErrorResponse(errorData)) {
				throw ApiError.fromApiErrorResponse(errorData);
			}
		}
		return (await response.json()) as Promise<T>;
	} catch (e: unknown) {
		if (e instanceof ApiError) {
			throw e;
		}
		console.error('Error fetching API:', e);
		throw new Error(`An unexpected error occurred fetching: ${endpoint}`);
	}
}
