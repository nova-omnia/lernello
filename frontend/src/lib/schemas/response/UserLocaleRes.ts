import { z } from 'zod';

export const UserLocaleResSchema = z.object({
	locale: z.string().nonempty()
});
export type UserLocaleRes = z.infer<typeof UserLocaleResSchema>;
