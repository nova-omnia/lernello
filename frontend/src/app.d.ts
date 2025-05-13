// See https://svelte.dev/docs/kit/types#app.d.ts

import type { UserInfo } from '$lib/schemas/response/UserInfo';

// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		interface Locals {
			isLoggedIn?: boolean;
			userInfo?: UserInfo;
		}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}
	}
}

export {};
