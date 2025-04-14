import { z } from 'zod';
import { LearningUnitResSchema } from '$lib/schemas/response/LearningUnitRes';

export const CreateLearningKitSchema = z.object({
	name: z.string().nonempty(),
	learningUnits: z.array(LearningUnitResSchema).nullable(),
	description: z.string().optional().nullable(),
	deadlineDate: z
		.string()
		.transform((val, ctx) => {
			const date = new Date(val);
			if (isNaN(date.getTime())) {
				ctx.addIssue({
					code: z.ZodIssueCode.custom,
					message: 'Invalid date format'
				});
				return z.NEVER;
			}
			return date.toISOString();
		})
		.optional()
		.nullable(),
	context: z.string().optional().nullable()
});
export type CreateLearningKit = z.infer<typeof CreateLearningKitSchema>;
