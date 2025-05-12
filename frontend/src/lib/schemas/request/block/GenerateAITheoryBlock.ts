import { z } from 'zod';

export const GenerateAITheoryBlockSchema = z.object({
	topic: z.string(),
	files: z.array(z.string().uuid())
});
export type GenerateAITheoryBlock = z.infer<typeof GenerateAITheoryBlockSchema>;
