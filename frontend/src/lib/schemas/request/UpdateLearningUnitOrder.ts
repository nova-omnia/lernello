import { z } from 'zod';

export const UpdateLearningUnitOrderSchema = z.object({
	learningUnitUuidsInOrder: z.array(z.string().uuid())
});
export type UpdateLearningUnitOrder = z.infer<typeof UpdateLearningUnitOrderSchema>;
