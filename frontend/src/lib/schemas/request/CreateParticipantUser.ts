import { z } from 'zod';

export const CreateParticipantUserSchema = z.object({
	username: z.string().email().nonempty(),
	name: z.string().min(3).max(40).nonempty(),
	surname: z.string().min(3).max(40).nonempty()
});
export type CreateParticipantUser = z.infer<typeof CreateParticipantUserSchema>;
