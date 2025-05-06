import { api } from '$lib/api/apiClient.js';
import { getLearningKits } from '$lib/api/collections/learningKit';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, parent, data }) => {
	const { queryClient } = await parent();

	await queryClient.prefetchQuery({
		queryKey: ['all-learning-kits-list'],
		queryFn: () => api(fetch).req(getLearningKits, null, { page: 0, size: 99999 }).parse()
	});

	return {
		role: data.role
	};
};
