import {z} from "zod";

export const CreateKitSchema = z.object({
    name : z.string().nonempty(),
    language : z.string().nonempty()
});
export type CreateKit = z.infer<typeof CreateKitSchema>;