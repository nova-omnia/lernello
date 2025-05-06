import { PasswordStatusSchema } from '$lib/schemas/response/PasswordStatus';
import { ParticipantUserSchema } from '$lib/schemas/response/ParticipantUser';
import { createEndpoint } from '../createEndpoint';
import { ChangePasswordDataSchema } from '$lib/schemas/request/ChangePasswordData';
import { z } from 'zod';
import { UserInfoSchema } from '$lib/schemas/response/UserInfo';
import { UserLocaleSchema } from '$lib/schemas/request/UserLocale';
import { CreateParticipantUserSchema } from '$lib/schemas/request/CreateParticipantUser';
import { CreateUserSchema } from '$lib/schemas/request/CreateUser';
import { UserSchema } from '$lib/schemas/response/UserSchema';

const REQUEST_MAPPING = '/api/user';

export const changePassword = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/password`,
	response: {
		schema: PasswordStatusSchema,
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
		schema: ParticipantUserSchema.array(),
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
		schema: ParticipantUserSchema.array(),
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

export const createUser = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}`,
	response: {
		schema: UserSchema,
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

export const editTrainee = createEndpoint({
	method: 'PATCH',
	getPath: () => `${REQUEST_MAPPING}/trainee`,
	response: {
		schema: ParticipantUserSchema,
		defaultValidate: true
	},
	payload: {
		schema: CreateParticipantUserSchema,
		defaultValidate: false
	}
});
