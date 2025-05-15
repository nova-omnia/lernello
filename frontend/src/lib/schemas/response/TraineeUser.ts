import { z } from 'zod';

export const TraineeUserSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	username: z.string().email().nonempty(),
	name: z.string().nonempty(),
	surname: z.string().nonempty()
});
export type TraineeUser = z.infer<typeof TraineeUserSchema>;
