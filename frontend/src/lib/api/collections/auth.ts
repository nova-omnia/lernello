import { UserLoginSchema } from '$lib/schemas/request/UserLogin';
import { LoggedInUserSchema } from '$lib/schemas/response/LoggedInUser';
import { z } from 'zod';
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

export const refreshToken = createEndpoint({
	method: 'GET',
	getPath: () => `${REQUEST_MAPPING}/refresh-token`,
	response: {
		schema: LoggedInUserSchema,
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});
