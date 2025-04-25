import { GenerateAITheoryBlockSchema } from "$lib/schemas/request/block/GenerateAITheoryBlock";
import { GeneratedAIQuestionBlockSchema } from "$lib/schemas/request/block/GeneratedAIQuestionBlock";
import { BlockResSchema } from "$lib/schemas/response/BlockRes";
import { createEndpoint } from "../createEndpoint";

const REQUEST_MAPPING = '/api/ai-block';
export const generateAITheoryBlock = createEndpoint({
    method: 'POST',
    getPath: () => `${REQUEST_MAPPING}/theory`,
    response: {
        schema: BlockResSchema,
        defaultValidate: true
    },
    payload: {
        schema: GenerateAITheoryBlockSchema,
        defaultValidate: false
    }
});


export const generatedAIQuestionBlock = createEndpoint({
    method: 'POST',
    getPath: () => `${REQUEST_MAPPING}/question`,
    response: {
        schema: BlockResSchema,
        defaultValidate: true
    },
    payload: {
        schema: GeneratedAIQuestionBlockSchema,
        defaultValidate: false
    }
});