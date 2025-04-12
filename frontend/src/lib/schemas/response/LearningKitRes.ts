import { z } from 'zod';
import { LearningUnitResSchema } from '$lib/schemas/response/LearningUnitRes';

export const LearningKitResSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	name: z.string().nonempty(),
	learningUnits: z.array(LearningUnitResSchema).nullable(),
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
