import {z} from "zod";
import {CreateLearningKitSchema} from "$lib/schemas/request/CreateLearningKit";

export const UpdateLearningKitSchema = CreateLearningKitSchema.extend({
    participants: z.array(z.string().uuid()).nullable(),
    files: z.array(z.string().uuid()).nullable()
});