import { z } from 'zod';

export const CreateLearningUnitSchema = z.object({
	name: z
		.string()
		.min(3, { message: 'Name must be at least 3 characters' })
		.max(40, { message: 'Name cannot exceed 40 characters' }),
	learningKitId: z.string().uuid({ message: 'Invalid learningKitId format' }).nonempty()
});
