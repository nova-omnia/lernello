import { GenericSuccessSchema } from '$lib/schemas/response/GenericSuccess';
import { TraineeUserSchema } from '$lib/schemas/response/TraineeUser';
import { createEndpoint } from '../createEndpoint';
import { ChangePasswordDataSchema } from '$lib/schemas/request/user/ChangePasswordData';
import { z } from 'zod';
import { UserInfoSchema } from '$lib/schemas/response/UserInfo';
import { UserLocaleSchema } from '$lib/schemas/request/user/UserLocale';
import { CreateTraineeUserSchema } from '$lib/schemas/request/user/CreateTraineeUser';
import { CreateUserSchema } from '$lib/schemas/request/user/CreateUser';
import { UserResSchema } from '$lib/schemas/response/UserResSchema';
import { UpdateUserSchema } from '$lib/schemas/request/user/UpdateUser';

const REQUEST_MAPPING = '/api/user';

export const changePassword = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/password`,
	response: {
		schema: GenericSuccessSchema,
		defaultValidate: true
	},
	payload: {
		schema: ChangePasswordDataSchema,
		defaultValidate: false
	}
});

export const getAllInstructors = createEndpoint({
	method: 'GET',
	getPath: () => `${REQUEST_MAPPING}/instructors`,
	response: {
		schema: TraineeUserSchema.array(),
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const getAllTrainees = createEndpoint({
	method: 'GET',
	getPath: () => `${REQUEST_MAPPING}/trainees`,
	response: {
		schema: TraineeUserSchema.array(),
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const getUserInfo = createEndpoint({
	method: 'GET',
	getPath: () => `${REQUEST_MAPPING}/info`,
	response: {
		schema: UserInfoSchema,
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const getUser = createEndpoint({
	method: 'GET',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}`,
	response: {
		schema: UserResSchema,
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const updateUser = createEndpoint({
	method: 'PATCH',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}`,
	response: {
		schema: UserResSchema,
		defaultValidate: true
	},
	payload: {
		schema: UpdateUserSchema,
		defaultValidate: false
	}
});

export const createUser = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/`,
	response: {
		schema: UserResSchema,
		defaultValidate: true
	},
	payload: {
		schema: CreateUserSchema,
		defaultValidate: false
	}
});

export const deleteUser = createEndpoint({
	method: 'DELETE',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}`,
	response: {
		schema: z.string().uuid(),
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const setUserLocale = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/locale`,
	response: {
		schema: UserLocaleSchema,
		defaultValidate: true
	},
	payload: {
		schema: UserLocaleSchema,
		defaultValidate: false
	}
});

export const resetUserPasswordAPI = createEndpoint({
	method: 'PATCH',
	getPath: (id: string) => `${REQUEST_MAPPING}/reset/${id}`,
	response: {
		schema: z.string().uuid(),
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const editTrainee = createEndpoint({
	method: 'PATCH',
	getPath: () => `${REQUEST_MAPPING}/trainee`,
	response: {
		schema: TraineeUserSchema,
		defaultValidate: true
	},
	payload: {
		schema: CreateTraineeUserSchema,
		defaultValidate: false
	}
});
