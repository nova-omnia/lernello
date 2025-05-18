import { z } from 'zod';
import { zodDateTimeString } from '$lib/utils/zodDateTimeString';

export const CreateLearningKitSchema = z.object({
	name: z.string().min(3).max(40).nonempty(),
	description: z.string().max(200).optional().nullable(),
	deadlineDate: zodDateTimeString().nullable().optional()
});
export type CreateLearningKit = z.infer<typeof CreateLearningKitSchema>;
