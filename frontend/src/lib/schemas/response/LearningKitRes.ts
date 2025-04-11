import { z } from 'zod';
import {LearningUnitResSchema} from "$lib/schemas/response/LearningUnitRes";

export const LearningKitResSchema = z.object({
	uuid: z.string().uuid(),
	name: z.string().nonempty(),
	learningUnits: z.array(LearningUnitResSchema).optional(),
	description: z.string().optional().nullable(),
	deadlineDate: z
		.string()
		.datetime({
			offset: true
		})
		.nullable(),
	context: z.string().nullable()
});
export type LearningKitRes = z.infer<typeof LearningKitResSchema>;
