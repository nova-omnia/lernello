import { z } from 'zod';
import {LearningUnitResSchema} from "$lib/schemas/response/LearningUnitRes";

export const LearningKitResSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	name: z.string().nonempty(),
	learningUnits: z.array(LearningUnitResSchema).optional(),
	description: z.string().optional(),
	deadlineDate: z.preprocess((val) => {
		if (typeof val === 'string' && val) return new Date(val);
		if (val instanceof Date) return val;
		return undefined;
	}, z.date().optional()),
	context: z.string().optional()
});
export type LearningKitRes = z.infer<typeof LearningKitResSchema>;
