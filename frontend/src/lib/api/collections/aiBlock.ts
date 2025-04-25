import { GenerateAITheoryBlockSchema } from "$lib/schemas/request/GenerateAITheoryBlock";
import { createEndpoint } from "../createEndpoint";

const REQUEST_MAPPING = '/api/ai-blocks';
export const generateAITheoryBlock = createEndpoint({
    method: 'POST',
    getPath: () => `${REQUEST_MAPPING}/`,
    response: {
        schema: GenerateAITheoryBlockSchema,
        defaultValidate: true
    },
    payload: {
        schema: GenerateAITheoryBlockSchema,
        defaultValidate: false
    }
});