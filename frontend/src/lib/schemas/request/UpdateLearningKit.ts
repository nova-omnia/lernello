import { z } from 'zod';

export const UpdateLearningKitSchema = z.object({
	name: z.string().min(3).max(40).optional(),
	description: z.string().nullable().optional(),
	deadlineDate: z.string().datetime().nullable().optional(),
	context: z.string().nullable().optional(),
	participants: z.array(z.string().uuid()).optional(),
	files: z.array(z.string().uuid()).optional()
});
export type UpdateLearningKit = z.infer<typeof UpdateLearningKitSchema>;
