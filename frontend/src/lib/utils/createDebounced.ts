// eslint-disable-next-line @typescript-eslint/no-explicit-any
type Procedure = (...args: any[]) => void;

export function createDebounced<T extends Procedure>(
	func: T,
	waitMilliseconds = 400
): (...args: Parameters<T>) => void {
	let timeoutId: ReturnType<typeof setTimeout> | undefined;

	return function debouncedFunction(...args: Parameters<T>): void {
		if (timeoutId !== undefined) {
			clearTimeout(timeoutId);
		}
		timeoutId = setTimeout(() => {
			func(...args);
		}, waitMilliseconds);
	};
}
