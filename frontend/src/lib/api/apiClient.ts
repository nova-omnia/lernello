import { ApiError, isApiErrorResponse } from './apiError';
import { getRequestEvent } from '$app/server';
import { recoverSession } from '$lib/server/auth';

// TODO handle different environments
const BASE_URL = 'http://localhost:8080';

type AllowedMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';

export async function request<T>(
	endpoint: string,
	method: AllowedMethod = 'GET',
	requestInit?: RequestInit
): Promise<T> {
	const url = new URL(endpoint, BASE_URL).toString();
	const response = await fetch(url, {
		method: method,
		headers: {
			'Content-Type': 'application/json',
			...requestInit?.headers
		},
		...requestInit
	});

	if (!response.ok) {
		const errorData = await response.json();
		if (isApiErrorResponse(errorData)) {
			throw ApiError.fromApiErrorResponse(errorData);
		}
	}

	return (await response.json()) as Promise<T>;
}

export async function userRequest<T>(
	endpoint: string,
	method: AllowedMethod = 'GET',
	requestInit?: RequestInit
): Promise<T> {
	recoverSession();
	const { locals } = getRequestEvent();
	if (!locals.user) {
		throw new Error('User is not authenticated');
	}

	// Call the existing request function with the updated headers
	return request(endpoint, method, {
		...requestInit,
		headers: {
			Authorization: `Bearer ${locals.user.token}`,
			...requestInit?.headers
		}
	});
}
