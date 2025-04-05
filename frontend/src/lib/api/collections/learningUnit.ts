import { LearningUnitResSchema } from '$lib/schemas/response/LearningUnitRes';
import { z } from 'zod';
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
