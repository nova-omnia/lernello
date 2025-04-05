import { createEndpoint } from '$lib/api/createEndpoint';
import { CreateUserLocaleSchema } from '$lib/schemas/request/CreateUserLocale';
import { UserLocaleResSchema } from '$lib/schemas/response/UserLocaleRes';
import { z } from 'zod';

const REQUEST_MAPPING = '/api/user/locale';

export const setUserLocale = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}`,
	response: {
		schema: UserLocaleResSchema,
		defaultValidate: true
	},
	payload: {
		schema: CreateUserLocaleSchema,
		defaultValidate: false
	}
});

export const getUserLocale = createEndpoint({
	method: 'GET',
	getPath: () => `${REQUEST_MAPPING}`,
	response: {
		schema: UserLocaleResSchema,
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});
