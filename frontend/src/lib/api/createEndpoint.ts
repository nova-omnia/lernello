import { type ZodSchema } from 'zod';

export type AllowedMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';

export function createEndpoint<TResSchema, TPathArgs extends unknown[], TPayloadSchema>({
	method,
	getPath,
	payload,
	response
}: {
	method: AllowedMethod;
	getPath: (...pathArgs: TPathArgs) => `/${string}`;
	payload: {
		schema: ZodSchema<TPayloadSchema>;
		defaultValidate: false; // always false for now, if any dev ever ends up here, do you REALLY need to change this?
	};
	response: {
		schema: ZodSchema<TResSchema>;
		defaultValidate: boolean;
	};
}): Endpoint<TResSchema, TPathArgs, TPayloadSchema> {
	return {
		method,
		getPath,
		payload,
		response
	};
}

export type Endpoint<TResSchema, TPathArgs extends unknown[], TPayloadSchema> = {
	method: AllowedMethod;
	getPath: (...pathArgs: TPathArgs) => `/${string}`;
	payload: {
		schema: ZodSchema<TPayloadSchema>;
		defaultValidate: false;
	};
	response: {
		schema: ZodSchema<TResSchema>;
		defaultValidate: boolean;
	};
};
