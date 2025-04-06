import { createEndpoint } from '$lib/api/createEndpoint';
import { UserLocaleSchema } from '$lib/schemas/request/CreateUserLocale';
import { z } from 'zod';

const REQUEST_MAPPING = '/api/user/locale';

export const setUserLocale = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}`,
	response: {
		schema: UserLocaleSchema,
		defaultValidate: true
	},
	payload: {
		schema: UserLocaleSchema,
		defaultValidate: false
	}
});

export const getUserLocale = createEndpoint({
	method: 'GET',
	getPath: () => `${REQUEST_MAPPING}`,
	response: {
		schema: UserLocaleSchema,
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});
