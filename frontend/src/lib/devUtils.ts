import { dev } from '$app/environment';

export function promiseDelay<T>(ms: number, chance: number = 0) {
	if (dev) {
		return (value: T) =>
			new Promise<T>((resolve, reject) => {
				setTimeout(() => {
					if (Math.random() < chance) {
						reject('Error');
					} else {
						resolve(value);
					}
				}, ms);
			});
	}
	return (value: T) => Promise.resolve(value);
}
