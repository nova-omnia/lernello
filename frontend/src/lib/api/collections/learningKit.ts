import { CreateLearningKitSchema } from '$lib/schemas/request/CreateLearningKit';
import { LearningKitResSchema } from '$lib/schemas/response/LearningKitRes';
import { createEndpoint } from '../createEndpoint';

const REQUEST_MAPPING = '/api/learning-kit';

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

// export const editLearningKit = createEndpoint({
// 	method: 'POST',
// 	getPath: () => `${REQUEST_MAPPING}/edit`,
// 	response: {
// 		schema: LearningKitResSchema,
// 		defaultValidate: true
// 	},
// 	payload: {
// 		schema: CreateLearningKitSchema,
// 		defaultValidate: false
// 	}
// });

// export const deleteLearningKit = createEndpoint({
// 	method: 'POST',
// 	getPath: () => `${REQUEST_MAPPING}/delete`,
// 	response: {
// 		schema: z.null(),
// 		defaultValidate: true
// 	},
// 	payload: {
// 		schema: CreateLearningKitSchema,
// 		defaultValidate: false
// 	}
// });

// export const getAllLearningKits = createEndpoint({
// 	method: 'POST',
// 	getPath: () => `${REQUEST_MAPPING}/getAll`,
// 	response: {
// 		schema: LearningKitResSchema.array(),
// 		defaultValidate: true
// 	},
// 	payload: {
// 		schema: z.null(),
// 		defaultValidate: false
// 	}
// });

// export const getLearningKitById = createEndpoint({
// 	method: 'POST',
// 	getPath: () => `${REQUEST_MAPPING}/getById`,
// 	response: {
// 		schema: LearningKitResSchema,
// 		defaultValidate: true
// 	},
// 	payload: {
// 		schema: CreateLearningKitSchema,
// 		defaultValidate: false
// 	}
// });
