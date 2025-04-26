import { z } from 'zod';

export const CreateLearningKitSchema = z.object({
	name: z.string().min(3).max(40).nonempty(),
	description: z.string().max(200).optional().nullable(),
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
