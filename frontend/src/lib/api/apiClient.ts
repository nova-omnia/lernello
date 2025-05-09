import type { z } from 'zod';
import type { AllowedMethod, Endpoint } from './createEndpoint';
import { ApiError, isApiErrorResponse } from './apiError';
import { browser } from '$app/environment';
import { PUBLIC_API_BASE_URL } from '$env/static/public';
import { apiClientAuthMiddleware } from './apiClientAuthMiddleware';

export const BASE_URL = PUBLIC_API_BASE_URL;

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
			...(requestInit?.body instanceof FormData ? {} : { 'Content-Type': 'application/json' }), // browser decides the content type for FormData
			...requestInit?.headers
		}
	} satisfies RequestInit;

	return {
		url,
		init: initObj
	};
}

async function parseResponse<TRes = unknown>(
	res: Promise<Response> | Response,
	schema: z.ZodSchema<TRes>,
	shouldValidate?: boolean
): Promise<TRes> {
	const response = await res;
	const data = await response.json();
	if (!response.ok) {
		if (isApiErrorResponse(data)) {
			throw ApiError.fromApiErrorResponse(data);
		}
	}
	if (shouldValidate) {
		return schema.parse(data);
	}
	return data as TRes;
}

export function api(fetchFn: typeof fetch) {
	function req<
		TResSchema,
		TPathArgs extends unknown[],
		TPayload,
		TRestEndpoint extends Endpoint<TResSchema, TPathArgs, TPayload>
	>(
		endpoint: TRestEndpoint,
		payload: z.infer<TRestEndpoint['payload']['schema']>,
		...pathArgs: TPathArgs
	): {
		response: Promise<Response>;
		parse: (shouldValidate?: boolean) => Promise<z.infer<TRestEndpoint['response']['schema']>>;
	} {
		if (endpoint.payload?.defaultValidate) {
			endpoint.payload.schema.parse(payload);
		}
		const { url, init } = buildRequestInit({
			endpoint: endpoint.getPath(...pathArgs),
			method: endpoint.method,
			requestInit: payload
				? {
						body: payload instanceof FormData ? payload : JSON.stringify(payload)
					}
				: undefined
		});

		let fetchFnWithMiddleware = fetchFn;

		if (browser) {
			fetchFnWithMiddleware = async (...args) => {
				const parsedToken = await apiClientAuthMiddleware();
				if (parsedToken) {
					init.headers = {
						...init.headers,
						Authorization: `Bearer ${parsedToken.token}`
					};
				}
				return fetchFn(...args);
			};
		}
		const response = fetchFnWithMiddleware(url, init);

		return {
			response,
			parse: async (shouldValidate?: boolean) => {
				return await parseResponse(
					response,
					endpoint.response.schema,
					shouldValidate ?? endpoint.response.defaultValidate
				);
			}
		};
	}

	return {
		req
	};
}
