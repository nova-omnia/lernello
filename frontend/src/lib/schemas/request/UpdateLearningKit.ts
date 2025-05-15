import { z } from 'zod';
import { zodDateTimeString } from '$lib/utils/zodDateTimeString';

export const UpdateLearningKitSchema = z.object({
	name: z.string().min(3).max(40).optional(),
	description: z.string().max(200).nullable().optional(),
	deadlineDate: zodDateTimeString().nullable().optional(),
	published: z.boolean().optional(),
	context: z.string().max(200).nullable().optional(),
	trainees: z.array(z.string().uuid()).optional(),
	files: z.array(z.string().uuid()).optional()
});
export type UpdateLearningKit = z.infer<typeof UpdateLearningKitSchema>;
