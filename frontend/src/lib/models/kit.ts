import { z } from 'zod';

export const CreateKitSchema = z.object({
	name: z.string().nonempty(),
	description: z.string().optional(),
	deadlineDate: z.preprocess((val) => {
		if (typeof val === 'string' && val) return new Date(val);
		if (val instanceof Date) return val;
		return undefined;
	}, z.date().optional()),
	language: z.string().nonempty(),
	participants: z.array(z.string().uuid()),
	context: z.string().optional()
	//files: z.array(z.object()) ToDo: Add File Objects
});
export type CreateKit = z.infer<typeof CreateKitSchema>;
export const LearningKitSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	name: z.string().nonempty(),
	learningUnits: z.array(z.any()).optional(), //ToDo
	description: z.string().optional(),
	deadlineDate: z.preprocess(
		(val) => (typeof val === 'string' ? new Date(val) : undefined),
		z.date().optional()
	),
	language: z.string().nonempty(),
	participants: z.array(z.string().uuid()),
	folderId: z.string().uuid().nullable()
	//files: z.array(z.object()) ToDo: Add File Objects
});
export type LearningKit = z.infer<typeof LearningKitSchema>;
