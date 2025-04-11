import { z } from 'zod';

export const ParticipantUserSchema = z.object({
	username: z.string().email().nonempty(),
	name: z.string().nonempty(),
	surname: z.string().nonempty(),
	uuid: z.string().uuid().nonempty()
});
export type ParticipantUser = z.infer<typeof ParticipantUserSchema>;
