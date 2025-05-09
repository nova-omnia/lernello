import { api } from '$lib/api/apiClient.js';
import type { PageLoad } from './$types';
import { getAllFiles } from '$lib/api/collections/file';

export const load: PageLoad = async ({ fetch, parent, data }) => {
	const { queryClient } = await parent();

	await queryClient.prefetchQuery({
		queryKey: ['files-overview'],
		queryFn: () => api(fetch).req(getAllFiles, null).parse()
	});

	return {
		role: data.role
	};
};
