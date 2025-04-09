import { z } from 'zod';

export const CreateLearningKitSchema = z.object({
	uuid: z.string().uuid().optional().nullable(),
	name: z.string().nonempty(),
	description: z.string().optional().nullable(),
	deadlineDate: z
		.preprocess((val) => {
			if (typeof val === 'string' && val) return new Date(val);
			if (val instanceof Date) return val;
			return undefined;
		}, z.date().optional())
		.nullable(),
	context: z.string().optional().nullable()
});
export type CreateLearningKit = z.infer<typeof CreateLearningKitSchema>;

export const EditLearningKitSchema = z.object({
	uuid: z.string().uuid().optional().nullable(),
	name: z.string().nonempty().nullable(),
	description: z.string().optional().nullable(),
	deadlineDate: z.string().optional().nullable(),
	context: z.string().optional().nullable()
});
export type EditLearningKit = z.infer<typeof EditLearningKitSchema>;
