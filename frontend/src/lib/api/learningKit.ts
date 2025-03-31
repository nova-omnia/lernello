import { userRequest } from '$lib/api/apiClient';
import { type CreateKit, type LearningKit, LearningKitSchema } from '$lib/models/kit';

export async function createLearningKit(payload: CreateKit): Promise<LearningKit> {
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

	return LearningKitSchema.parse(loginUserRes);
}
