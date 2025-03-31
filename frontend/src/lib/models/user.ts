import { z } from 'zod';

export const UserLoginSchema = z.object({
	username: z.string().email().nonempty(),
	password: z.string().min(8).nonempty()
});
export type UserLogin = z.infer<typeof UserLoginSchema>;

export const UserTokenSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	token: z.string().nonempty(),
	changedPassword: z.boolean(),
	expires: z.number().int().nonnegative()
});
export type UserToken = z.infer<typeof UserTokenSchema>;

export const ParticipantSchema = z.object({
	username: z.string().email().nonempty(),
	uuid: z.string().uuid().nonempty()
});
export type Participant = z.infer<typeof ParticipantSchema>;