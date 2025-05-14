import { api } from '$lib/api/apiClient.js';
import { handleApiError } from '$lib/api/apiError.js';
import { getLearningUnitById } from '$lib/api/collections/learningUnit';
import { markLearningUnitOpened } from '$lib/api/collections/progress.js';
import { TRAINEE_ROLE } from '$lib/schemas/response/UserInfo.js';
import type { LearningUnitProgressRes } from '$lib/schemas/response/progress/LearningUnitProgressResSchema';
import type { UserInfo } from '$lib/schemas/response/UserInfo';

export const load = handleApiError(async ({ params, fetch, locals }) => {
	const { learningUnitId } = params;
	const userInfo = locals.userInfo as UserInfo | undefined; // Access userInfo from locals

	const learningUnit = await api(fetch).req(getLearningUnitById, null, learningUnitId).parse();
	let initialLearningUnitProgress: LearningUnitProgressRes | null = null;

	if (userInfo && userInfo.role === TRAINEE_ROLE && learningUnitId) {
		try {
			initialLearningUnitProgress = await api(fetch)
				.req(markLearningUnitOpened, { learningUnitId })
				.parse();
		} catch (error) {
			console.error(
				`Failed to mark learning unit ${learningUnitId} as opened or fetch progress in server load:`,
				error
			);
		}
	}

	return {
		learningUnitId,
		learningUnit,
		initialLearningUnitProgress,
		userInfo
	};
});
