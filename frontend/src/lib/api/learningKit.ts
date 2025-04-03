import { userRequest } from '$lib/api/apiClient';
import type { CreateLearningKit } from '$lib/schemas/request/CreateLearningKit';
import { LearningKitResSchema, type LearningKitRes } from '$lib/schemas/response/LearningKitRes';

export async function createLearningKit(payload: CreateLearningKit): Promise<LearningKitRes> {
	const { deadlineDate, ...rest } = payload;
	const loginUserRes = await userRequest(`/api/learning-kits/create`, 'POST', {
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			...rest,
			deadlineDate: deadlineDate ? deadlineDate.toISOString().split('T')[0] : null
		})
	});

	return LearningKitResSchema.parse(loginUserRes);
}
