import { z } from 'zod';

export const CreateLearningUnitSchema = z.object({
	name: z.string()
		.min(2, { message: 'Name must be at least 2 characters' })
		.max(32, { message: 'Name cannot exceed 32 characters' })
});