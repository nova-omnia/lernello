<script lang="ts">
	import { ArrowUpDown, CheckCircle2, Users } from 'lucide-svelte';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import UserItem from '$lib/components/learningkit/displays/UserItem.svelte';
	import { createQuery } from '@tanstack/svelte-query';
	import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';
	import { api } from '$lib/api/apiClient';
	import { getLearningKitById } from '$lib/api/collections/learningKit';
	import type { LearningKitProgressRes } from '$lib/schemas/response/progress/LearningKitProgressResSchema';
	import { getLearningKitProgressForAllParticipants } from '$lib/api/collections/progress';
	import { _ } from 'svelte-i18n';

	interface LearningKitStatisticsProps {
		learningKitId: string;
	}

	const { learningKitId }: LearningKitStatisticsProps = $props();

	const kitQuery = createQuery<LearningKitRes>({
		queryKey: ['learning-kit-stats-detail', learningKitId],
		queryFn: () => api(fetch).req(getLearningKitById, null, learningKitId).parse()
	});

	const progressQuery = createQuery<LearningKitProgressRes[]>({
		queryKey: ['learning-kit-participants-progress-stats', learningKitId],
		queryFn: () =>
			api(fetch).req(getLearningKitProgressForAllParticipants, null, learningKitId).parse()
	});

	interface ParticipantWithProgress {
		userId: string;
		uuid: string;
		username: string;
		name: string;
		surname: string;
		progressPercentage: number;
		isCompleted: boolean;
	}

	type SortField = 'name' | 'progress';
	type SortDirection = 'asc' | 'desc';

	let sortField: SortField = $state('name');
	let sortDirection: SortDirection = $state('asc');

	let participantsWithProgress = $derived.by(() => {
		if ($kitQuery.isSuccess && $kitQuery.data && $progressQuery.isSuccess && $progressQuery.data) {
			const kit = $kitQuery.data;
			const progresses = $progressQuery.data;

			const enrichedParticipants =
				kit.participants?.map((p) => {
					const participantProgress = progresses.find((prog) => prog.userId === p.uuid);
					return {
						userId: p.uuid,
						uuid: p.uuid,
						username: p.username,
						name: p.name,
						surname: p.surname,
						progressPercentage: participantProgress?.progressPercentage ?? 0,
						isCompleted: participantProgress?.isCompleted ?? false
					} as ParticipantWithProgress;
				}) ?? [];

			let sortedParticipants = [...enrichedParticipants];
			if (sortField === 'name') {
				sortedParticipants.sort((a, b) => {
					const nameA = `${a.name} ${a.surname}`.toLowerCase().trim();
					const nameB = `${b.name} ${b.surname}`.toLowerCase().trim();
					return sortDirection === 'asc' ? nameA.localeCompare(nameB) : nameB.localeCompare(nameA);
				});
			} else if (sortField === 'progress') {
				sortedParticipants.sort((a, b) => {
					return sortDirection === 'asc'
						? a.progressPercentage - b.progressPercentage
						: b.progressPercentage - a.progressPercentage;
				});
			}
			return sortedParticipants;
		}
		return [];
	});

	const averageProgress = $derived($kitQuery.data?.averageProgress ?? 0);

	const completionRate = $derived($kitQuery.data?.completionRate ?? 0);

	function toggleSort(field: SortField) {
		if (sortField === field) {
			sortDirection = sortDirection === 'asc' ? 'desc' : 'asc';
		} else {
			sortField = field;
			sortDirection = 'asc';
		}
	}
</script>

{#if $kitQuery.isPending || $progressQuery.isPending}
	<div class="space-y-4">
		<div class="placeholder h-8 w-1/2"></div>
		<div class="grid grid-cols-1 gap-4 md:grid-cols-2">
			<div class="placeholder card h-24"></div>
			<div class="placeholder card h-24"></div>
		</div>
		<div class="placeholder h-6 w-1/4"></div>
		{#each Array(3)}
			<div class="card placeholder flex h-16 items-center justify-between p-4"></div>
		{/each}
	</div>
{:else if $kitQuery.isError || $progressQuery.isError}
	<ErrorIllustration>
		<p class="text-error-500 text-center">{$_('statistics.error.loadDetail')}</p>
	</ErrorIllustration>
{:else if $kitQuery.data && $progressQuery.data}
	<div class="space-y-6">
		<section class="card p-4">
			<h2 class="preset-typo-subtitle mb-4">{$_('statistics.kitOverview.title', {
				values: { name: $kitQuery.data.name }
			})}</h2>
			<div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
				<div class="bg-surface-100-800 flex items-center space-x-4 rounded-lg p-4">
					<div class="bg-primary-500 rounded-full p-3 text-white">
						<Users size={24} />
					</div>
					<div>
						<p class="text-surface-500-400 text-sm">
							{$_('statistics.averageProgress')}
						</p>
						<p class="text-2xl font-bold">{averageProgress}%</p>
					</div>
				</div>
				<div class="bg-surface-100-800 flex items-center space-x-4 rounded-lg p-4">
					<div class="bg-secondary-500 rounded-full p-3 text-white">
						<CheckCircle2 size={24} />
					</div>
					<div>
						<p class="text-surface-500-400 text-sm">{$_('statistics.completionRate')}</p>
						<p class="text-2xl font-bold">{completionRate}%</p>
					</div>
				</div>
			</div>
		</section>

		<section class="card p-6">
			<div class="mb-4 flex items-center justify-between">
				<h2 class="preset-typo-subtitle">{$_('statistics.trainees.title')}</h2>
				<div class="flex space-x-2">
					<button class="btn preset-tonal-surface btn-sm" onclick={() => toggleSort('name')}>
						<span>{$_('common.name')}</span>
						{#if sortField === 'name'}
							<ArrowUpDown size={16} class={sortDirection === 'desc' ? 'rotate-180' : ''} />
						{/if}
					</button>
					<button class="btn preset-tonal-surface btn-sm" onclick={() => toggleSort('progress')}>
						<span>{$_('common.progress')}</span>
						{#if sortField === 'progress'}
							<ArrowUpDown size={16} class={sortDirection === 'desc' ? 'rotate-180' : ''} />
						{/if}
					</button>
				</div>
			</div>

			{#if participantsWithProgress.length === 0}
				<p class="text-surface-500-400 text-center">
					{$_('statistics.trainees.none')}
				</p>
			{:else}
				<ul class="space-y-3">
					{#each participantsWithProgress as participant (participant.userId)}
						<UserItem
							user={participant}
							onRemoveUser={() => {
								console.warn(
									'onRemoveUser triggered on statistics page for user:',
									participant.username
								);
							}}
							isUsersView={false}
							showProgress={true}
							progressPercentage={participant.progressPercentage}
							isCompleted={participant.isCompleted}
						/>
					{/each}
				</ul>
			{/if}
		</section>
	</div>
{:else}
	<ErrorIllustration>
		<p class="text-error-500 text-center">{$_('statistics.error.noData')}</p>
	</ErrorIllustration>
{/if}
