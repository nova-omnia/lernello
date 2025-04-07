import type { ZodSchema } from 'zod';
import type { AllowedMethod } from './createApiClient';

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
}): RestEndpoint<TResSchema, TPathArgs, TPayloadSchema> {
	return {
		method,
		getPath,
		payload,
		response
	};
}

export type RestEndpoint<TResSchema, TPathArgs extends unknown[], TPayloadSchema> = {
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
