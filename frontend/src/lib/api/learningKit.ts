import {request} from '$lib/api/apiClient';
import {type CreateKit, type LearningKit, LearningKitSchema} from "$lib/models/kit";

export async function createLearningKit(payload: CreateKit): Promise<LearningKit> {
    const loginUserRes = await request(`/api/learningKit`, 'POST', {
        body: JSON.stringify(payload)
    });
    return LearningKitSchema.parse(loginUserRes);
}
