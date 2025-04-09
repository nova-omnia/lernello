import { z } from 'zod';

export const LearningKitResSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	name: z.string().nonempty(),
	learningUnits: z.array(z.any()).optional().nullable(), //ToDo
	description: z.string().optional().nullable(),
	deadlineDate: z.preprocess((val) => {
		if (typeof val === 'string' && val) return new Date(val);
		if (val instanceof Date) return val;
		return undefined;
	}, z.date()).nullable(),
	context: z.string().optional().nullable()
});
export type LearningKitRes = z.infer<typeof LearningKitResSchema>;
