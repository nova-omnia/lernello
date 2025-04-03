import z from 'zod';

export const LearningUnitResSchema = z.object({
	uuid: z.string().uuid(),
	name: z.string()
});
export type LearningUnitRes = z.infer<typeof LearningUnitResSchema>;
