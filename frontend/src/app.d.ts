// See https://svelte.dev/docs/kit/types#app.d.ts

import type { LoggedInUser } from '$lib/schemas/response/LoggedInUser';
import type { UserInfo } from '$lib/schemas/response/UserInfo';

// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		interface Locals {
			tokenInfo?: LoggedInUser;
			userInfo?: UserInfo;
		}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}
	}
}

export {};
