import { createEndpoint } from '$lib/api/createEndpoint';
import { UserLocaleSchema } from '$lib/schemas/request/UserLocale';

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
