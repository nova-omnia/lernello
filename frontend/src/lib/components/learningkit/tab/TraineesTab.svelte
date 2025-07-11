<script lang="ts">
	import { UserPlus } from 'lucide-svelte';
	import MultiSelect from '$lib/components/select/MultiSelect.svelte';
	import { _ } from 'svelte-i18n';
	import { createMutation, createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getAllTrainees } from '$lib/api/collections/user';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import type { UpdateLearningKit } from '$lib/schemas/request/UpdateLearningKit';
	import { updateLearningKit } from '$lib/api/collections/learningKit';
	import UserItem from '$lib/components/learningkit/displays/UserItem.svelte';
	import type { TraineeUser } from '$lib/schemas/response/TraineeUser';
	import { toaster } from '$lib/states/toasterState.svelte';

	const invalidate = useQueryInvalidation();

	interface TraineesTabProps {
		learningKitId: string;
		trainees: TraineeUser[];
	}

	const { learningKitId, trainees }: TraineesTabProps = $props();

	const availableTraineesQuery = createQuery({
		queryKey: ['trainees-list'],
		queryFn: () => api(fetch).req(getAllTrainees, null).parse(),
		staleTime: 0,
		refetchOnMount: true
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

	function onRemove(traineeUuid: string) {
		const updated = trainees.filter((t) => t.uuid !== traineeUuid).map((t) => t.uuid);

		$updateLearningKitMutation.mutate({
			id: learningKitId,
			data: {
				trainees: updated
			}
		});

		toaster.create({
			description: $_('user.remove.success'),
			type: 'success'
		});
	}
</script>

<div class="flex w-full flex-col gap-4 p-4">
	<div class="flex w-full justify-between gap-4">
		<div>
			<h2 class="preset-typo-subtitle">{$_('common.trainees')}</h2>
			<p>{$_('learningKit.trainees.description')}</p>
		</div>
		<a
			class="btn preset-outlined-surface-500 h-fit"
			href={`/learningkit/${learningKitId}/create-trainee`}
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
			selected={trainees.map((trainee) => ({
				uuid: trainee.uuid,
				label: `${trainee.username} | ${trainee.name} ${trainee.surname}`
			}))}
			onSelect={(options) => {
				$updateLearningKitMutation.mutate({
					id: learningKitId,
					data: {
						trainees: options.map((opt) => opt.uuid)
					}
				});
			}}
		/>
		<div class="flex flex-col gap-1">
			{#each trainees ?? [] as trainee (trainee.uuid)}
				<UserItem user={trainee} onRemoveUser={() => onRemove(trainee.uuid)} />
			{/each}
		</div>
	</div>
</div>
