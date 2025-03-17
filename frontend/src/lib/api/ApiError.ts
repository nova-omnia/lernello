export interface ApiErrorResponse {
	status: number;
	message: string;
	error: string;
	[key: string]: unknown;
}

export function isApiErrorResponse(obj: unknown): obj is ApiErrorResponse {
	return (
		!!obj &&
		typeof obj === 'object' &&
		typeof (obj as ApiErrorResponse).status === 'number' &&
		(typeof (obj as ApiErrorResponse).message === 'string' || !(obj as ApiErrorResponse).message) &&
		typeof (obj as ApiErrorResponse).error === 'string'
	);
}

export class ApiError extends Error {
	status: number;
	error: string;
	details?: unknown;

	constructor(status: number, message: string, error: string, details?: unknown) {
		super(message);
		this.status = status;
		this.details = details;
		this.error = error;
		Object.setPrototypeOf(this, ApiError.prototype);
	}

	toApiErrorResponse(): ApiErrorResponse {
		return {
			status: this.status,
			message: this.message,
			error: this.error
		};
	}

	static fromApiErrorResponse(response: ApiErrorResponse): ApiError {
		const { status, message, error, ...rest } = response;
		return new ApiError(status, message, error, rest);
	}
}
