import {
	LoggedInUserNoRefreshSchema,
	type LoggedInUserNoRefresh
} from '$lib/schemas/response/LoggedInUser';

export function setNewTokenData(loggedInUserNoRefresh: LoggedInUserNoRefresh) {
	localStorage.setItem('lernello_auth_token', JSON.stringify(loggedInUserNoRefresh));
}

function parseTokenFromLocalStorage() {
	const OFFSET = 1000; // short offset to avoid edge cases

	const token = localStorage.getItem('lernello_auth_token');
	if (!token) {
		return false;
	}
	const parsedToken = LoggedInUserNoRefreshSchema.parse(JSON.parse(token));

	const tokenExp = new Date(parsedToken.expires).getTime() - Date.now();
	const refreshTokenExp = new Date(parsedToken.refreshExpires).getTime() - Date.now();

	return {
		isExpired: tokenExp <= OFFSET,
		refreshIsExpired: refreshTokenExp <= OFFSET,
		parsedToken
	};
}

const IS_REFRESHING_KEY = 'lernello_auth_token_is_refreshing';

function isRefreshingAlready() {
	const isRefreshing = localStorage.getItem(IS_REFRESHING_KEY);

	if (isRefreshing) {
		const timestamp = parseInt(isRefreshing, 10);
		const now = Date.now();
		const REFRESH_TIMEOUT = 4000;

		if (now - timestamp > REFRESH_TIMEOUT) {
			localStorage.removeItem(IS_REFRESHING_KEY);
			return false;
		}
		return true;
	}

	return false;
}

type ApiClientMiddlewareReturn = false | LoggedInUserNoRefresh;

export async function apiClientAuthMiddleware(): Promise<ApiClientMiddlewareReturn> {
	const parsedData = parseTokenFromLocalStorage();
	if (!parsedData) {
		return false;
	}
	const { isExpired, refreshIsExpired, parsedToken } = parsedData;

	if (refreshIsExpired) {
		localStorage.removeItem('lernello_auth_token');
		window.location.reload();
		return false;
	}

	if (isExpired) {
		await new Promise((resolve) => setTimeout(resolve, 1)); // tick to avoid sync code edge cases

		const isRefreshing = isRefreshingAlready();
		if (!isRefreshing) {
			localStorage.setItem(IS_REFRESHING_KEY, Date.now().toString());
			console.log('waiting for token refresh - first time');
		} else {
			console.log('waiting for token refresh - already refreshing');
			return new Promise<ApiClientMiddlewareReturn>((resolve) => {
				setTimeout(async () => {
					const result = await apiClientAuthMiddleware();
					resolve(result);
				}, 400);
			});
		}

		try {
			const data = await fetch('/refresh', {
				method: 'GET'
			});
			const json = await data.json();
			const loggedInUserNoRefresh = LoggedInUserNoRefreshSchema.parse(json);
			setNewTokenData(loggedInUserNoRefresh);
		} catch (error) {
			console.error('Error refreshing token:', error);
		} finally {
			console.log('finished refreshing token');

			localStorage.removeItem(IS_REFRESHING_KEY);
		}
		const freshParsedData = parseTokenFromLocalStorage();
		if (!freshParsedData) {
			window.location.reload(); // something went wrong
			return false;
		}
		return freshParsedData.parsedToken;
	} else {
		return parsedToken;
	}
}
