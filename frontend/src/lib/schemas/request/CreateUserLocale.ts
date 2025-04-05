import { z } from 'zod';

export const CreateUserLocaleSchema = z.object({
	locale: z.string().nonempty()
});
export type CreateUserLocale = z.infer<typeof CreateUserLocaleSchema>;
