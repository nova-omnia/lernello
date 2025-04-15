import { z } from 'zod';
import { LearningUnitResSchema } from '$lib/schemas/response/LearningUnitRes';
import { BlockActionSchema } from '$lib/schemas/request/BlockAction';
import { createEndpoint } from '../createEndpoint';
import { CreateLearningUnitSchema } from '$lib/schemas/request/CreateLearningUnit';

const REQUEST_MAPPING = '/api/learning-unit';

export const getLearningUnitById = createEndpoint({
	method: 'GET',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}`,
	response: {
		schema: LearningUnitResSchema,
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});
export const deleteLearningUnit = createEndpoint({
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
export const regenerateLearningUnit = createEndpoint({
	method: 'POST',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}/regenerate`,
	response: {
		schema: LearningUnitResSchema,
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const applyBlockActions = createEndpoint({
	method: 'POST',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}/apply-block-actions`,
	response: {
		schema: z.record(z.string(), z.string()),
		defaultValidate: true
	},
	payload: {
		schema: z.array(BlockActionSchema),
		defaultValidate: false
	}
});

export const createLearningUnit = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/create`,
	response: {
		schema: LearningUnitResSchema,
		defaultValidate: true
	},
	payload: {
		schema: CreateLearningUnitSchema,
		defaultValidate: false
	}
});
