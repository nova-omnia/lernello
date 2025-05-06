<script lang="ts">
	import UserItem from '$lib/components/learningkit/displays/UserItem.svelte';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { deleteUser, getAllInstructors } from '$lib/api/collections/user';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import { _ } from 'svelte-i18n';

	const invalidate = useQueryInvalidation();

	const instructorsQuery = createQuery({
		queryKey: ['instructors-overview-list'],
		queryFn: () => api(fetch).req(getAllInstructors, null).parse()
	});

	const deleteInstructorMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(deleteUser, null, id).parse(),
		onSuccess: () => {
			invalidate(['instructors-overview-list']);
		}
	});
</script>

<div class="flex w-full flex-col gap-4 p-4">
	<div class="flex flex-col gap-1">
		{#if $instructorsQuery.status === 'pending'}
			{#each Array(3)}
				<div class="card preset-filled-surface-100-900 h-20 w-full gap-2 p-5">
					<div class="placeholder h-full animate-pulse"></div>
				</div>
			{/each}
		{:else if $instructorsQuery.status === 'error'}
			<ErrorIllustration>{$_('trainees.error.loadList')}</ErrorIllustration>
		{:else}
			{#each $instructorsQuery.data ?? [] as trainee (trainee.uuid)}
				<UserItem
					user={trainee}
					onRemoveUser={() => $deleteInstructorMutation.mutate(trainee.uuid)}
				/>
			{/each}
		{/if}
	</div>
	<div class="flex flex-col gap-1">
		{#each $instructorsQuery.data ?? [] as instructor (instructor.uuid)}
			<UserItem
				user={instructor}
				onRemoveUser={() => $deleteInstructorMutation.mutate(instructor.uuid)}
			/>
		{/each}
	</div>
</div>
