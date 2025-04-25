import { AIGeneratedMultipleChoiceSchema } from '$lib/schemas/request/AIGeneratedMutipleChoice';
import { BlockResSchema } from '$lib/schemas/response/BlockRes';
import { createEndpoint } from '../createEndpoint';

const REQUEST_MAPPING = '/api/ai-block';
export const generateMultipleChoice = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/multiple-choice`,
	response: {
		schema: BlockResSchema,
		defaultValidate: true
	},
	payload: {
		schema: AIGeneratedMultipleChoiceSchema,
		defaultValidate: false
	}
});
