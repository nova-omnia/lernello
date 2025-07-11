import { CreateLearningKitSchema } from '$lib/schemas/request/CreateLearningKit';
import { UpdateLearningKitSchema } from '$lib/schemas/request/UpdateLearningKit';
import { LearningKitPageSchema, LearningKitResSchema } from '$lib/schemas/response/LearningKitRes';
import { createEndpoint } from '../createEndpoint';
import { z } from 'zod';
import { CreateTraineeUserSchema } from '$lib/schemas/request/user/CreateTraineeUser';
import { UpdateLearningUnitOrderSchema } from '$lib/schemas/request/UpdateLearningUnitOrder';
import { GenericSuccessSchema } from '$lib/schemas/response/GenericSuccess';

const REQUEST_MAPPING = '/api/learning-kits';

export const createLearningKit = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/`,
	response: {
		schema: LearningKitResSchema,
		defaultValidate: true
	},
	payload: {
		schema: CreateLearningKitSchema,
		defaultValidate: false
	}
});

export const deleteLearningKit = createEndpoint({
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

export const getLearningKits = createEndpoint({
	method: 'GET',
	getPath: ({ page = 0, size = 100 } = {}) => `${REQUEST_MAPPING}/?page=${page}&size=${size}`,
	response: {
		schema: LearningKitPageSchema,
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const getLearningKitById = createEndpoint({
	method: 'GET',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}`,
	response: {
		schema: LearningKitResSchema,
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const updateLearningKit = createEndpoint({
	method: 'PATCH',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}`,
	response: {
		schema: LearningKitResSchema,
		defaultValidate: true
	},
	payload: {
		schema: UpdateLearningKitSchema,
		defaultValidate: false
	}
});

export const addTraineeInLearningKit = createEndpoint({
	method: 'POST',
	getPath: (id: string) => `${REQUEST_MAPPING}/trainee/${id}`,
	response: {
		schema: z.string().uuid(),
		defaultValidate: true
	},
	payload: {
		schema: CreateTraineeUserSchema,
		defaultValidate: false
	}
});

export const publishLearningKit = createEndpoint({
	method: 'POST',
	getPath: (id: string) => `${REQUEST_MAPPING}/publish/${id}`,
	response: {
		schema: z.null(),
		defaultValidate: false
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const updateLearningUnitsOrder = createEndpoint({
	method: 'PATCH',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}/reorder/learning-units/`,
	response: {
		schema: GenericSuccessSchema,
		defaultValidate: true
	},
	payload: {
		schema: UpdateLearningUnitOrderSchema,
		defaultValidate: false
	}
});
