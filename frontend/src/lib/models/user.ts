import { z } from 'zod';

export const UserLoginSchema = z.object({
	username: z.string().email().nonempty(),
	password: z.string().min(8).nonempty()
});
export type UserLogin = z.infer<typeof UserLoginSchema>;

export const UserTokenSchema = z.object({
	username: z.string().nonempty(),
	token: z.string().nonempty(),
	expires: z.number().int().nonnegative()
});
export type UserToken = z.infer<typeof UserTokenSchema>;

export const CreateKitSchema = z.object({
	name : z.string().nonempty(),
	language : z.string().nonempty()
});
export type CreateKit = z.infer<typeof CreateKitSchema>;