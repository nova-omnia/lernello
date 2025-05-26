import { z } from 'zod';

export const UserLocaleSchema = z.object({
	locale: z.string().nonempty()
});
export type UserLocale = z.infer<typeof UserLocaleSchema>;
