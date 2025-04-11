import { getAllLearningKits } from '$lib/api/collections/learningKit';
import { serverApiClient } from '$lib/api/serverApiClient.js';

export async function load({ depends }) {
	depends('learningkits:list');
	const kits = await serverApiClient.req(getAllLearningKits, null);
	return { kits };
}
