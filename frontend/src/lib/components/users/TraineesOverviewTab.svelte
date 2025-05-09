<script lang="ts">
	import UserItem from '$lib/components/learningkit/displays/UserItem.svelte';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { deleteUser, getAllTrainees } from '$lib/api/collections/user';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import { _ } from 'svelte-i18n';
	import { toaster } from '$lib/states/toasterState.svelte';

	const invalidate = useQueryInvalidation();

	const traineesQuery = createQuery({
		queryKey: ['trainees-overview-list'],
		queryFn: () => api(fetch).req(getAllTrainees, null).parse()
	});

	const deleteTraineeMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(deleteUser, null, id).parse(),
		onSuccess: () => {
			invalidate(['trainees-overview-list']);
			toaster.create({
				title: $_('common.success.title'),
				description: $_('trainees.overview.delete.success'),
				type: 'success'
			});
		},
		onError: (error) => {
			console.error('Error deleting trainee:', error);
			toaster.create({
				title: $_('common.error.title'),
				description: $_('error.description', { values: { status: 'unknown' } }),
				type: 'error'
			});
		}
	});
</script>

<div class="flex w-full flex-col gap-4 p-4">
	<div class="flex flex-col gap-1">
		{#if $traineesQuery.status === 'pending'}
			{#each Array(3)}
				<div class="card preset-filled-surface-100-900 h-20 w-full gap-2 p-5">
					<div class="placeholder h-full animate-pulse"></div>
				</div>
			{/each}
		{:else if $traineesQuery.status === 'error'}
			<ErrorIllustration>{$_('trainees.error.loadList')}</ErrorIllustration>
		{:else}
			{#each $traineesQuery.data ?? [] as trainee (trainee.uuid)}
				<UserItem
					usersView={true}
					user={trainee}
					onRemoveUser={() => $deleteTraineeMutation.mutate(trainee.uuid)}
				/>
			{/each}
		{/if}
	</div>
</div>
