import { z } from "zod";


export const AddTraineeSchema = z.object({
    email: z.string().email().nonempty(),
    name: z.string().nonempty(),
    surname: z.string().nonempty()});
                
export type AddTrainee = z.infer<typeof AddTraineeSchema>;