import { ApiError, isApiErrorResponse } from './apiError';

//TODO change BASE_URL
const BASE_URL = 'http://localhost:8080';

type AllowedMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';

export async function request<T>(
	endpoint: string,
	method: AllowedMethod = 'GET',
	request?: RequestInit
): Promise<T> {
	const url = new URL(endpoint, BASE_URL).toString();
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
}
