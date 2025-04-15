import { z } from 'zod';

export const FileResSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	name: z.string().nonempty()
});
export type FileRes = z.infer<typeof FileResSchema>;
