import { CreateLearningKitSchema } from '$lib/schemas/request/CreateLearningKit';
import { LearningKitResSchema } from '$lib/schemas/response/LearningKitRes';
import { createEndpoint } from '../createEndpoint';
import { z } from 'zod';

const REQUEST_MAPPING = '/api/learning-kits';

export const createLearningKit = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/create`,
	response: {
		schema: LearningKitResSchema,
		defaultValidate: true
	},
	payload: {
		schema: CreateLearningKitSchema,
		defaultValidate: false
	}
});

export const editLearningKit = createEndpoint({
	method: 'PUT',
	getPath: () => `${REQUEST_MAPPING}/edit`,
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

export const getAllLearningKits = createEndpoint({
	method: 'GET',
	getPath: () => `${REQUEST_MAPPING}/`,
	response: {
		schema: LearningKitResSchema.array(),
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
