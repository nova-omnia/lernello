import { api } from '$lib/api/apiClient.js';
import { getLearningKits } from '$lib/api/collections/learningKit';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, parent }) => {
	const { queryClient } = await parent();

	await queryClient.prefetchQuery({
		queryKey: ['latest-learning-kits-list'],
		queryFn: () => api(fetch).req(getLearningKits, null, { size: 5, page: 0 }).parse()
	});
};
