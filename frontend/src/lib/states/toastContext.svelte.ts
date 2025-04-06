import { getContext } from 'svelte';
import { type ToastContext } from '@skeletonlabs/skeleton-svelte';

export function getToastContext(): ToastContext {
	return getContext('toast');
}
