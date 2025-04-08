import { z } from 'zod';

export const LearningKitResSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	name: z.string().nonempty(),
	learningUnits: z.array(z.any()).optional(), //ToDo
	description: z.string().optional(),
	deadlineDate: z.preprocess((val) => {
		if (typeof val === 'string' && val) return new Date(val);
		if (val instanceof Date) return val;
		return undefined;
	}, z.date().optional()),
	context: z.string().optional()
});
export type LearningKitRes = z.infer<typeof LearningKitResSchema>;
