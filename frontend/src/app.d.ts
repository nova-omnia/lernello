// See https://svelte.dev/docs/kit/types#app.d.ts

import type { UserToken } from '$lib/models/user';

// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		interface Locals {
			user?: UserToken;
		}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}
	}
}

export {};
