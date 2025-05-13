import { z } from 'zod';

export const ParticipantUserSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	username: z.string().email().nonempty(),
	name: z.string().nonempty(),
	surname: z.string().nonempty()
});
export type ParticipantUser = z.infer<typeof ParticipantUserSchema>;
