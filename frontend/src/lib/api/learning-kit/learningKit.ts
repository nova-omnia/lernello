import { userRequest } from '$lib/api/apiClient';
import { type CreateKit, type LearningKit, LearningKitSchema } from '$lib/models/kit';
import {List} from "lucide-svelte";
import {z} from "zod";
import {ParticipantSchema} from "$lib/models/user";

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

export async function getLearningKit(uuid: string): Promise<LearningKit> {
	const res = await userRequest(`/api/learning-kits/${uuid}`, 'GET');
	return LearningKitSchema.parse(res);
}

export async function getAllLearningKits(): Promise<Array<LearningKit>> {
	const res = await userRequest(`/api/learning-kits/getAll`, 'GET');
	return z.array(LearningKitSchema).parse(res);
}

export async function updateLearningKit(payload: CreateKit): Promise<LearningKit> {
	const { deadlineDate, ...rest } = payload;
	const res = await userRequest(`/api/learning-kits/edit`, 'PUT', {
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			...rest,
			deadlineDate: deadlineDate ? deadlineDate.toISOString().split('T')[0] : null
		})
	});
	return LearningKitSchema.parse(res);
}