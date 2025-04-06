import { z } from 'zod';
import { LearningUnitResSchema } from '$lib/schemas/response/LearningUnitRes';
import { BlockActionSchema } from '$lib/schemas/request/blockAction';
import { createEndpoint } from '../createEndpoint';

const REQUEST_MAPPING = '/api/learning-kit/learning-unit';

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

export const applyBlockActions = createEndpoint({
	method: 'POST',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}/applyLearningUnitActions`,
	response: {
		schema: z.record(z.string(), z.string()),
		defaultValidate: true
	},
	payload: {
		schema: z.array(BlockActionSchema),
		defaultValidate: false
	}
});
