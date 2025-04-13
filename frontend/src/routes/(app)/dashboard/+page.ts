import { api } from '$lib/api/apiClient.js';
import { getAllLearningKits } from '$lib/api/collections/learningKit';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, parent }) => {
	const { queryClient } = await parent();

	await queryClient.prefetchQuery({
		queryKey: ['learning-kits', 'list'],
		queryFn: () => api(fetch).req(getAllLearningKits, null).parse()
	});
};
