import { z } from 'zod';

export const CreateLearningKitSchema = z.object({
	uuid: z.string().uuid().optional(),
	name: z.string().nonempty(),
	description: z.string().optional(),
	deadlineDate: z.preprocess((val) => {
		if (typeof val === 'string' && val) return new Date(val);
		if (val instanceof Date) return val;
		return undefined;
	}, z.date().optional()),
	context: z.string().optional()
});
export type CreateLearningKit = z.infer<typeof CreateLearningKitSchema>;

export const EditLearningKitSchema = z.object({
	uuid: z.string().uuid().optional(),
	name: z.string().nonempty(),
	description: z.string().optional(),
	deadlineDate: z.string(),
	context: z.string().optional()
});
export type EditLearningKit = z.infer<typeof EditLearningKitSchema>;