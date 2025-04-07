import { z } from 'zod';

export const LearningKitResSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	name: z.string().nonempty(),
	learningUnits: z.array(z.any()).optional(), //ToDo
	description: z.string().optional().nullable(),
	deadlineDate: z.preprocess(
		(val) => (typeof val === 'string' ? new Date(val) : undefined),
		z.date().optional()
	),
	language: z.string().optional().nullable(),
	participants: z.array(z.string().uuid()),
	folderId: z.string().uuid().optional().nullable()
});
export type LearningKitRes = z.infer<typeof LearningKitResSchema>;