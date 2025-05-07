import { z } from 'zod';
import { createEndpoint } from '$lib/api/createEndpoint';
import { CheckMultipleChoiceAnswerSchema } from '$lib/schemas/request/progress/CheckMultipleChoiceAnswerSchema';
import { MultipleChoiceAnswerValidationResSchema } from '$lib/schemas/response/progress/MultipleChoiceAnswerValidationResSchema';
import { LearningUnitOpenedSchema } from '$lib/schemas/request/progress/LearningUnitOpenedSchema';
import { LearningUnitProgressResSchema } from '$lib/schemas/response/progress/LearningUnitProgressResSchema';
import { LearningKitOpenedSchema } from '$lib/schemas/request/progress/LearningKitOpenedSchema';
import { LearningKitProgressResSchema } from '$lib/schemas/response/progress/LearningKitProgressResSchema';
import { CheckQuestionAnswerSchema } from '$lib/schemas/request/progress/CheckQuestionAnswerSchema';
import { QuestionAnswerValidationResSchema } from '$lib/schemas/response/progress/QuestionAnswerValidationResSchema';

const REQUEST_MAPPING = '/api/progress';

export const markLearningKitOpened = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/learning-kit/opened`,
	payload: {
		schema: LearningKitOpenedSchema,
		defaultValidate: false
	},
	response: {
		schema: LearningKitProgressResSchema,
		defaultValidate: true
	}
});

export const markLearningUnitOpened = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/learning-unit/opened`,
	payload: {
		schema: LearningUnitOpenedSchema,
		defaultValidate: false
	},
	response: {
		schema: LearningUnitProgressResSchema,
		defaultValidate: true
	}
});

export const checkMultipleChoiceAnswer = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/block/multiple-choice/check`,
	payload: {
		schema: CheckMultipleChoiceAnswerSchema,
		defaultValidate: false
	},
	response: {
		schema: MultipleChoiceAnswerValidationResSchema,
		defaultValidate: true
	}
});

export const checkQuestionAnswer = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/block/question/check`,
	payload: {
		schema: CheckQuestionAnswerSchema,
		defaultValidate: false
	},
	response: {
		schema: QuestionAnswerValidationResSchema,
		defaultValidate: true
	}
});

export const getLearningKitProgress = createEndpoint({
	method: 'GET',
	getPath: (learningKitId: string) => `${REQUEST_MAPPING}/learning-kit/${learningKitId}`,
	response: {
		schema: LearningKitProgressResSchema,
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const getLearningUnitProgress = createEndpoint({
	method: 'GET',
	getPath: (learningUnitId: string) => `${REQUEST_MAPPING}/learning-unit/${learningUnitId}`,
	response: {
		schema: LearningUnitProgressResSchema,
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});
