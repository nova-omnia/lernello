import z from 'zod';

export const LearningUnitSchema = z.object({
	uuid: z.string().uuid(),
	name: z.string()
});
export type LearningUnit = z.infer<typeof LearningUnitSchema>;
