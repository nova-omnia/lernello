import { PasswordStatusSchema } from '$lib/schemas/response/PasswordStatus';
import { ParticipantUserSchema } from '$lib/schemas/response/ParticipantUser';
import { createEndpoint } from '../createEndpoint';
import { ChangePasswordDataSchema } from '$lib/schemas/request/ChangePasswordData';
import { z } from 'zod';
import { UserInfoSchema } from '$lib/schemas/response/UserInfo';
import { UserLocaleSchema } from '$lib/schemas/request/UserLocale';
import { CreateParticipantUserSchema } from '$lib/schemas/request/CreateParticipantUser';

const REQUEST_MAPPING = '/api/user';

export const changePassword = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/change-password`,
	response: {
		schema: PasswordStatusSchema,
		defaultValidate: true
	},
	payload: {
		schema: ChangePasswordDataSchema,
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

export const addTrainee = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/trainee/add`,
	response: {
		schema: ParticipantUserSchema,
		defaultValidate: true
	},
	payload: {
		schema: CreateParticipantUserSchema,
		defaultValidate: false
	}
});

export const deleteTrainee = createEndpoint({
	method: 'DELETE',
	getPath: (id: string) => `${REQUEST_MAPPING}/trainee/delete/${id}`,
	response: {
		schema: z.null(),
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
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/edit`,
	response: {
		schema: ParticipantUserSchema,
		defaultValidate: true
	},
	payload: {
		schema: CreateParticipantUserSchema,
		defaultValidate: false
	}
});
