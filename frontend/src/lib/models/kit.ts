import {z} from "zod";

export const CreateKitSchema = z.object({
    name : z.string().nonempty(),
    description : z.string().optional(),
    deadline : z.date().optional(),
    language : z.string().nonempty(),
    context : z.string().optional()
});
export type CreateKit = z.infer<typeof CreateKitSchema>;