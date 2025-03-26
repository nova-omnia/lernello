import { request } from '$lib/api/apiClient';
import {CreateKitSchema,type CreateKit} from "$lib/models/kit";

export async function createLearningKit(payload: CreateKit): Promise<CreateKit> {
    const loginUserRes = await request(`/api/learningKit`, 'POST', {
        body: JSON.stringify(payload)
    });
    const learningkit = CreateKitSchema.parse(loginUserRes);
    return learningkit;
}
