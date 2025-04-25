import {z} from 'zod';

export const GenerateAITheoryBlockSchema = z.object({
    blockId: z.string().nonempty(),
    topic : z.string().nonempty(),
    files: z.array(z.string().uuid())
});
export type GenerateAITheoryBlock = z.infer<typeof GenerateAITheoryBlockSchema>;