import { z } from 'zod';

/**
 * Zod-Helper to validate and transform a valid ISO8601 date.
 * Transforms e.g. "2025-04-27T14:30" correctly to "2025-04-27T14:30:00.000Z".
 */

export const zodDateTimeString = () =>
	z.string().transform((val, ctx) => {
		const date = new Date(val);
		if (isNaN(date.getTime())) {
			ctx.addIssue({
				code: z.ZodIssueCode.custom,
				message: 'Invalid date format'
			});
			return z.NEVER;
		}
		return date.toISOString();
	});
