import { z } from 'zod';

export const CreateParticipantUserSchema = z.object({
	username: z.string().email().nonempty(),
	name: z
		.string()
		.min(3, { message: 'Name must be at least 3 characters' })
		.max(40, { message: 'Name cannot exceed 40 characters' })
		.nonempty(),
	surname: z
		.string()
		.min(3, { message: 'Name must be at least 3 characters' })
		.max(40, { message: 'Name cannot exceed 40 characters' })
		.nonempty()
});
export type CreateParticipantUser = z.infer<typeof CreateParticipantUserSchema>;
