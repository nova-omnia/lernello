import { useQueryClient } from '@tanstack/svelte-query';

export function useQueryInvalidation() {
	const queryClient = useQueryClient();

	const invalidate = (queryKey: string[]) => {
		queryClient.invalidateQueries({
			queryKey
		});
	};

	return invalidate;
}
