import { UserLoginSchema } from '$lib/schemas/request/user/UserLogin';
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
