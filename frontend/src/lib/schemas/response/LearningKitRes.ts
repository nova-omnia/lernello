import { z } from 'zod';

export const LearningKitResSchema = z.object({
	uuid: z.string().uuid(),
	name: z.string().nonempty(),
	description: z.string().nullable(),
	deadlineDate: z
		.string()
		.datetime({
			offset: true
		})
		.nullable(),
	context: z.string().nullable()
});
export type LearningKitRes = z.infer<typeof LearningKitResSchema>;
