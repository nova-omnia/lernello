<script lang="ts">
	import TraineeItem from '$lib/components/learningkit/displays/TraineeItem.svelte';
	import MultiSelect from '$lib/components/MultiSelect.svelte';
	import { UserPlus } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getAllTrainees } from '$lib/api/collections/user';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import type { UpdateLearningKit } from '$lib/schemas/request/UpdateLearningKit';
	import { updateLearningKit } from '$lib/api/collections/learningKit';

	const invalidate = useQueryInvalidation();

	interface TraineesTabProps {
		learningKitId: string;
		participants: {
			uuid: string;
			username: string;
			name: string;
			surname: string;
		}[];
	}

	const { learningKitId, participants }: TraineesTabProps = $props();

	const availableTraineesQuery = createQuery({
		queryKey: ['trainees-list'],
		queryFn: () => api(fetch).req(getAllTrainees, null).parse()
	});

	const updateLearningKitMutation = createMutation({
		mutationFn: ({ id, data }: { id: string; data: UpdateLearningKit }) =>
			api(fetch).req(updateLearningKit, data, id).parse(),
		onSuccess: () => {
			invalidate(['latest-learning-kits-list']);
			invalidate(['all-learning-kits-list']);
			invalidate(['learning-kit', learningKitId]);
		}
	});
</script>

<div class="flex w-full flex-col gap-4 p-4">
	<div class="flex w-full justify-between gap-4">
		<div>
			<h2 class="preset-typo-subtitle">{$_('common.trainees')}</h2>
			<p>{$_('learningKit.trainees.description')}</p>
		</div>
		<a
			class="btn preset-outlined-surface-500 h-fit"
			href={`/learningkit/${learningKitId}/create-trainee?learningKitId=${learningKitId}`}
		>
			<UserPlus size={24} />
			{$_('addTrainee')}
		</a>
	</div>

	<div class="flex flex-col gap-4">
		<MultiSelect
			options={$availableTraineesQuery.data?.map((trainee) => ({
				uuid: trainee.uuid,
				label: `${trainee.username} | ${trainee.name} ${trainee.surname}`
			})) ?? []}
			selected={participants.map((trainee) => ({
				uuid: trainee.uuid,
				label: `${trainee.username} | ${trainee.name} ${trainee.surname}`
			}))}
			onSelect={(options) => {
				$updateLearningKitMutation.mutate({
					id: learningKitId,
					data: {
						participants: options.map((opt) => opt.uuid)
					}
				});
			}}
		/>
		<div class="flex flex-col gap-1">
			{#each participants ?? [] as trainee (trainee.uuid)}
				<TraineeItem
					user={trainee}
					onRemoveTrainee={() => {
						const updated = participants.filter((t) => t.uuid !== trainee.uuid).map((t) => t.uuid);

						$updateLearningKitMutation.mutate({
							id: learningKitId,
							data: {
								participants: updated
							}
						});
					}}
				/>
			{/each}
		</div>
	</div>
</div>
