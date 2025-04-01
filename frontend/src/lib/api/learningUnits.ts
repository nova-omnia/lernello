import { userRequest } from '$lib/api/apiClient';
import type { LearningUnit } from '$lib/models/learningUnit';

const BASE_ENDPOINT = '/api/learning-units';

export async function createLearningUnit(name: string): Promise<LearningUnit> {
	return userRequest<LearningUnit>(`${BASE_ENDPOINT}/create`, 'POST', {
		body: JSON.stringify({ name })
	});
}

export async function deleteLearningUnit(id: string): Promise<void> {
	return userRequest<void>(`${BASE_ENDPOINT}/${id}`, 'DELETE');
}

export async function getLearningUnitById(id: string): Promise<LearningUnit> {
	return userRequest<LearningUnit>(`${BASE_ENDPOINT}/${id}`);
}

export async function getAllLearningUnits(): Promise<LearningUnit[]> {
	return userRequest<LearningUnit[]>(`${BASE_ENDPOINT}/all`);
}

export async function updateBlockOrder(id: string, blockIds: string[]): Promise<LearningUnit> {
	return userRequest<LearningUnit>(`${BASE_ENDPOINT}/${id}/blocks/order`, 'PUT', {
		body: JSON.stringify(blockIds)
	});
}

async function addBlock(id: string, blockId: string): Promise<LearningUnit> {
	return userRequest<LearningUnit>(`${BASE_ENDPOINT}/${id}/blocks/add`, 'POST', {
		body: JSON.stringify({ blockId })
	});
}

export const addTheoryBlock = () => addBlock('New Theory Block', 'theory');
export const addMultipleChoiceQuizBlock = () => addBlock('New Multiple Choice Block', 'quiz');
export const addTextAnswerQuizBlock = () => addBlock('New Text Answer Block', 'quiz');

export async function deleteBlock(id: string, blockId: string): Promise<LearningUnit> {
	return userRequest<LearningUnit>(`${BASE_ENDPOINT}/${id}/blocks/${blockId}`, 'DELETE');
}
