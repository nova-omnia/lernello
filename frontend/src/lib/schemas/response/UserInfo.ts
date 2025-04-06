import { z } from 'zod';

export const UserInfoSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	username: z.string().nonempty(),
	locale: z.string().nullable(),
	changedPassword: z.boolean()
});
export type UserInfo = z.infer<typeof UserInfoSchema>;
