import type { z } from 'zod';
import type { RestEndpoint } from './createEndpoint';
import { ApiError, isApiErrorResponse } from './apiError';
import { browser } from '$app/environment';
import { LoggedInUserSchema } from '$lib/schemas/response/LoggedInUser';

// TODO handle different environments
export const BASE_URL = 'http://localhost:8080';

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
		...requestInit,
		headers: {
			'Content-Type': 'application/json',
			...requestInit?.headers
		}
	} satisfies RequestInit;

	return {
		url,
		init: initObj
	};
}

export function api(fetchFn: typeof fetch) {
	return {
		req: <
			TResSchema,
			TPathArgs extends unknown[],
			TPayload,
			TRestEndpoint extends RestEndpoint<TResSchema, TPathArgs, TPayload>
		>(
			endpoint: TRestEndpoint,
			payload: z.infer<TRestEndpoint['payload']['schema']>,
			requestInit?: RequestInit,
			...pathArgs: TPathArgs
		): {
			response: Promise<Response>;
			parse: (shouldValidate?: boolean) => Promise<z.infer<TRestEndpoint['response']['schema']>>;
		} => {
			if (endpoint.payload?.defaultValidate) {
				endpoint.payload.schema.parse(payload);
			}
			const { url, init } = buildRequestInit({
				endpoint: endpoint.getPath(...pathArgs),
				method: endpoint.method,
				requestInit: payload
					? {
							...requestInit,
							body: JSON.stringify(payload)
						}
					: requestInit
			});

			if (browser) {
				const token = localStorage.getItem('lernello_auth_token');
				if (token) {
					console.log(token);

					const parsedToken = LoggedInUserSchema.parse(JSON.parse(token));
					init.headers = {
						...init.headers,
						Authorization: `Bearer ${parsedToken.token}`
					};
				}
			}
			const response = fetchFn(url, init);

			return {
				response,
				parse: async (shouldValidate?: boolean) => {
					const resp = await response;
					const data = await resp.json();
					if (!resp.ok) {
						if (isApiErrorResponse(data)) {
							throw ApiError.fromApiErrorResponse(data);
						}
					}
					if (
						(endpoint.response.defaultValidate && shouldValidate !== false) ||
						shouldValidate === true
					) {
						return endpoint.response.schema.parse(data);
					}
					return data as Promise<z.infer<TRestEndpoint['response']['schema']>>;
				}
			};
		}
	};
}
