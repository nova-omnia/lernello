import { api } from '$lib/api/apiClient.js';
import { getLearningKits } from '$lib/api/collections/learningKit';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, parent, data }) => {
	const { queryClient } = await parent();

	// Prefetch for "Latest Learning Kits" section (if still needed separately)
	await queryClient.prefetchQuery({
		queryKey: ['latest-learning-kits-list'],
		queryFn: () => api(fetch).req(getLearningKits, null, { size: 5, page: 0 }).parse()
	});

	// Prefetch all (or a larger set of) kits for the statistics overview
	await queryClient.prefetchQuery({
		queryKey: ['all-instructor-kits-for-dashboard-stats'],
		// Fetch a larger number, e.g., 100, to have enough data for sorting
		queryFn: () => api(fetch).req(getLearningKits, null, { size: 100, page: 0 }).parse()
	});

	return {
		role: data.role
	};
};
