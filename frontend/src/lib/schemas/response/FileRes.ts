import { z } from 'zod';
import { PageMetaSchema } from './PageMeta';

export const FileResSchema = z.object({
	uuid: z.string().uuid().nonempty(),
	name: z.string().nonempty()
});
export type FileRes = z.infer<typeof FileResSchema>;
export const FilePageSchema = PageMetaSchema.extend({
	content: z.array(FileResSchema)
});
export type FilePage = z.infer<typeof FilePageSchema>;

