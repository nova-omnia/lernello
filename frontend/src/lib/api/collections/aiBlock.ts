import { GenerateAITheoryBlockSchema } from '$lib/schemas/request/GenerateAITheoryBlock';
import { BlockResSchema } from '$lib/schemas/response/BlockRes';
import { createEndpoint } from '../createEndpoint';

const REQUEST_MAPPING = '/api/ai-block';
export const generateAITheoryBlock = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/theoryBlock`,
	response: {
		schema: BlockResSchema,
		defaultValidate: true
	},
	payload: {
		schema: GenerateAITheoryBlockSchema,
		defaultValidate: false
	}
});
