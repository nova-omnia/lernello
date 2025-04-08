import { createToaster } from '@skeletonlabs/skeleton-svelte';

export const toaster = $state(
	createToaster({
		placement: 'bottom-end'
	})
);
