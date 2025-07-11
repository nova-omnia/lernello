import { z } from 'zod';
import { LearningUnitResSchema } from '$lib/schemas/response/LearningUnitRes';
import { BlockActionSchema } from '$lib/schemas/request/block/BlockAction';
import { createEndpoint } from '../createEndpoint';
import { CreateLearningUnitSchema } from '$lib/schemas/request/CreateLearningUnit';
import { GenerateAILearningUnitSchema } from '$lib/schemas/request/GenerateAILearningUnit';
import { RenameLearningUnitSchema } from '$lib/schemas/request/RenameLearningUnit';

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

export const renameLearningUnit = createEndpoint({
	method: 'POST',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}/rename`,
	response: {
		schema: LearningUnitResSchema,
		defaultValidate: true
	},
	payload: {
		schema: RenameLearningUnitSchema,
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
	getPath: () => `${REQUEST_MAPPING}/`,
	response: {
		schema: LearningUnitResSchema,
		defaultValidate: true
	},
	payload: {
		schema: CreateLearningUnitSchema,
		defaultValidate: false
	}
});

export const generateLearningUnit = createEndpoint({
	method: 'POST',
	getPath: (id: string) => `${REQUEST_MAPPING}/${id}/generate-async`,
	response: {
		schema: z.object({ jobId: z.string() }),
		defaultValidate: true
	},
	payload: {
		schema: GenerateAILearningUnitSchema,
		defaultValidate: false
	}
});

export const getGenerationStatus = createEndpoint({
	method: 'GET',
	getPath: (jobId: string) => `${REQUEST_MAPPING}/generation-status/${jobId}`,
	response: {
		schema: z.object({
			status: z.enum(['PENDING', 'IN_PROGRESS', 'DONE', 'FAILED']),
			message: z.string().nullable().optional()
		}),
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});

export const getActiveJobId = createEndpoint({
	method: 'GET',
	getPath: (unitId: string) => `${REQUEST_MAPPING}/active-job/${unitId}`,
	response: {
		schema: z.object({ jobId: z.string() }),
		defaultValidate: true
	},
	payload: {
		schema: z.null(),
		defaultValidate: false
	}
});
