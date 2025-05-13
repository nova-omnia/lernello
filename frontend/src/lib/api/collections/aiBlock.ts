import { GenerateAITheoryBlockSchema } from '$lib/schemas/request/block/GenerateAITheoryBlock';
import { GeneratedAIQuestionBlockSchema } from '$lib/schemas/request/block/GeneratedAIQuestionBlock';
import {
	MultipleChoiceBlockResSchema,
	QuestionBlockResSchema,
	TheoryBlockResSchema
} from '$lib/schemas/response/BlockRes';
import { createEndpoint } from '../createEndpoint';

const REQUEST_MAPPING = '/api/ai-block';
export const generateAITheoryBlock = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/theory`,
	response: {
		schema: TheoryBlockResSchema.omit({ uuid: true, name: true, position: true }),
		defaultValidate: true
	},
	payload: {
		schema: GenerateAITheoryBlockSchema,
		defaultValidate: false
	}
});

export const generatedAIQuestionBlock = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/question-block`,
	response: {
		schema: QuestionBlockResSchema.omit({ uuid: true, name: true, position: true }),
		defaultValidate: true
	},
	payload: {
		schema: GeneratedAIQuestionBlockSchema,
		defaultValidate: false
	}
});

export const generatedAIMultipleChoiceBlock = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/multiple-choice`,
	response: {
		schema: MultipleChoiceBlockResSchema.omit({ uuid: true, name: true, position: true }),
		defaultValidate: true
	},
	payload: {
		schema: GeneratedAIQuestionBlockSchema,
		defaultValidate: false
	}
});
