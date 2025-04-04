import type { z } from 'zod';
import { ApiError, isApiErrorResponse } from './apiError';
import type { RestEndpoint } from './createEndpoint';

// TODO handle different environments
const BASE_URL = 'http://localhost:8080';

export type AllowedMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';

function buildRequestInit({
	endpoint,
	method,
	requestInit
}: {
	endpoint: string;
	method: AllowedMethod;
	requestInit?: RequestInit;
}) {
	const url = new URL(endpoint, BASE_URL).toString();
	const initObj: RequestInit = {
		method,
		headers: {
			'Content-Type': 'application/json',
			...requestInit?.headers
		},
		...requestInit
	} satisfies RequestInit;

	return {
		url,
		init: initObj
	};
}

async function fetchWrapper<T>(url: string, init: RequestInit): Promise<T> {
	const response = await fetch(url, init);
	if (!response.ok) {
		const errorData = await response.json();
		if (isApiErrorResponse(errorData)) {
			throw ApiError.fromApiErrorResponse(errorData);
		}
	}
	return (await response.json()) as Promise<T>;
}

export function createApiClient(requestInitMiddleware: (request: RequestInit) => RequestInit) {
	async function req<
		TResSchema,
		TPathArgs extends unknown[],
		TPayload,
		TRestEndpoint extends RestEndpoint<TResSchema, TPathArgs, TPayload>
	>(
		endpoint: TRestEndpoint,
		payload: z.infer<TRestEndpoint['payload']['schema']>,
		...pathArgs: TPathArgs
	): Promise<z.infer<TRestEndpoint['response']['schema']>> {
		if (endpoint.payload?.defaultValidate) {
			endpoint.payload.schema.parse(payload);
		}

		const { url, init } = buildRequestInit({
			endpoint: endpoint.getPath(...pathArgs),
			method: endpoint.method,
			requestInit: payload
				? {
						body: JSON.stringify(payload)
					}
				: undefined
		});
		const response = await fetchWrapper(url, requestInitMiddleware(init));
		if (endpoint.response.defaultValidate) {
			return endpoint.response.schema.parse(response);
		}
		return response as TResSchema;
	}
	return { req };
}

export type ApiClient = ReturnType<typeof createApiClient>;
