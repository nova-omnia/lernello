import { z } from 'zod';
import { CreateLearningKitSchema } from '$lib/schemas/request/CreateLearningKit';

export const UpdateLearningKitSchema = CreateLearningKitSchema.extend({
	name: z.string().min(3).max(40).optional(),
	description: z.string().nullable().optional(),
	deadlineDate: z.string().datetime().nullable().optional(),
	context: z.string().nullable().optional(),
	participants: z.array(z.string().uuid()).optional(),
	files: z.array(z.string().uuid()).optional()
});
export type UpdateLearningKit = z.infer<typeof UpdateLearningKitSchema>;
