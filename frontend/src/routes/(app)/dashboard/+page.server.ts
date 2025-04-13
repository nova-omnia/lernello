import { apiClient } from '$lib/api/apiClient.js';
import { getAllLearningKits } from '$lib/api/collections/learningKit';

export async function load({ depends, fetch }) {
	depends('learningkits:list');
	const kits = await apiClient(getAllLearningKits, null).load(fetch).parse();

	return { kits };
}
