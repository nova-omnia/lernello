import z from 'zod';

export const LearningUnitResSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	name: z.string().nonempty()
});
export type LearningUnitRes = z.infer<typeof LearningUnitResSchema>;
