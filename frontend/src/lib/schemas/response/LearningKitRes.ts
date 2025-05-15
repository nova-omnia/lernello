import { z } from 'zod';
import { LearningUnitResSchema } from '$lib/schemas/response/LearningUnitRes';
import { TraineeUserSchema } from './TraineeUser';
import { FileResSchema } from './FileRes';
import { PageMetaSchema } from '$lib/schemas/response/PageMeta';

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
	published: z.boolean(),
	context: z.string().nullable(),
	trainees: z.array(TraineeUserSchema),
	files: z.array(FileResSchema).nullable(),
	averageProgress: z.number(),
	completionRate: z.number()
});
export type LearningKitRes = z.infer<typeof LearningKitResSchema>;
export const LearningKitPageSchema = PageMetaSchema.extend({
	content: z.array(LearningKitResSchema)
});
export type LearningKitPage = z.infer<typeof LearningKitPageSchema>;
