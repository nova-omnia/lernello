import type { z } from 'zod';
import type { AllowedMethod, Endpoint } from './createEndpoint';
import { ApiError, isApiErrorResponse } from './apiError';
import { browser } from '$app/environment';
import { LoggedInUserSchema } from '$lib/schemas/response/LoggedInUser';

// TODO handle different environments
export const BASE_URL = 'http://localhost:8080';

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

		if (browser) {
			const token = localStorage.getItem('lernello_auth_token');
			if (token) {
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
