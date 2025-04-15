import { z } from 'zod';
import { CreateLearningKitSchema } from '$lib/schemas/request/CreateLearningKit';

export const UpdateLearningKitSchema = CreateLearningKitSchema.extend({
	learningKitId: z.string().uuid().nonempty(),
	participants: z.array(z.string().uuid()).nullable(),
	files: z.array(z.string().uuid()).nullable()
});
