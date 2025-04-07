import { UserLoginSchema } from '$lib/schemas/request/UserLogin';
import { LoggedInUserSchema } from '$lib/schemas/response/LoggedInUser';
import { createEndpoint } from '../createEndpoint';

const REQUEST_MAPPING = '/api/auth';

export const signin = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/signin`,
	response: {
		schema: LoggedInUserSchema,
		defaultValidate: true
	},
	payload: {
		schema: UserLoginSchema,
		defaultValidate: false
	}
});

export const setAuthCookie = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/set-cookie`,
	payload: {
		schema: LoggedInUserSchema,
		defaultValidate: false
	},
	response: {
		schema: LoggedInUserSchema,
		defaultValidate: true
	}
});
