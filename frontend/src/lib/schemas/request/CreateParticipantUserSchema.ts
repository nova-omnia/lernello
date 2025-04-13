import { z } from 'zod';

export const CreateParticipantUserSchema = z.object({
    username: z.string().email().nonempty(),
    name: z.string().nonempty(),
    surname: z.string().nonempty()
});
export type CreateParticipantUser = z.infer<typeof CreateParticipantUserSchema>;