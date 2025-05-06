import { FileResSchema } from '$lib/schemas/response/FileRes';
import { createEndpoint } from '../createEndpoint';
import { z } from 'zod';

const REQUEST_MAPPING = '/api/files';

export const uploadFile = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/`,
	payload: {
		schema: z.custom<FormData>((val: unknown) => {
			if (val instanceof FormData) {
				return val.has('file');
			}
			return false;
		}),
		defaultValidate: false
	},
	response: {
		schema: FileResSchema,
		defaultValidate: true
	}
});

export const getFileById = createEndpoint({
	method: 'GET',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}`,
	payload: {
		schema: z.null(),
		defaultValidate: false
	},
	response: {
		schema: FileResSchema,
		defaultValidate: true
	}
});

export const deleteFile = createEndpoint({
	method: 'DELETE',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}`,
	payload: {
		schema: z.null(),
		defaultValidate: false
	},
	response: {
		schema: z.null(),
		defaultValidate: true
	}
});

export const getAllFiles = createEndpoint({
	method: 'GET',
	getPath: () => `${REQUEST_MAPPING}/`,
	payload: {
		schema: z.null(),
		defaultValidate: false
	},
	response: {
		schema: FileResSchema.array(),
		defaultValidate: true
	}
});
