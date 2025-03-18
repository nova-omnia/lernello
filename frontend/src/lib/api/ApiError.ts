import { error } from '@sveltejs/kit';
import { z } from 'zod';

export const ApiErrorResponseSchema = z.object({
	status: z.number().int().nonnegative(),
	message: z.string().nonempty(),
	error: z.string().nonempty(),
	timestamp: z.string().nonempty(),
	path: z.string().nonempty(),
	trace: z.string().optional()
});

export type ApiErrorResponse = z.infer<typeof ApiErrorResponseSchema>;

export function isApiErrorResponse(data: unknown): data is ApiErrorResponse {
	return ApiErrorResponseSchema.safeParse(data).success;
}

export class ApiError extends Error {
	status: number;
	error: string;
	timestamp: string;
	path: string;
	trace?: string;

	constructor(
		status: number,
		message: string,
		error: string,
		timestamp: string,
		path: string,
		trace?: string
	) {
		super(message);
		this.status = status;
		this.error = error;
		this.timestamp = timestamp;
		this.path = path;
		this.trace = trace;

		// Set the prototype explicitly.
		Object.setPrototypeOf(this, ApiError.prototype);
		this.name = 'ApiError';
	}

	toApiErrorResponse(): ApiErrorResponse {
		return {
			status: this.status,
			message: this.message,
			error: this.error,
			timestamp: this.timestamp,
			path: this.path,
			trace: this.trace
		};
	}

	static fromApiErrorResponse(response: ApiErrorResponse): ApiError {
		return new ApiError(
			response.status,
			response.message,
			response.error,
			response.timestamp,
			response.path,
			response.trace
		);
	}
}

export function handleApiError<TRes, TArgs extends unknown[]>(handler: (...args: TArgs) => TRes) {
	return async (...args: TArgs) => {
		try {
			return await handler(...args);
		} catch (err) {
			if (err instanceof ApiError) {
				error(err.status, err.toApiErrorResponse());
			}

			console.error(err);
			error(500, {
				message: 'An unexpected error occurred'
			});
		}
	};
}
