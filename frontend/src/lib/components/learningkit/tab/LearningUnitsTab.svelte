<script lang="ts">
	import { _ } from 'svelte-i18n';
	import { createMutation } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { deleteLearningUnit } from '$lib/api/collections/learningUnit';
	import { useQueryInvalidation } from '$lib/api/useQueryInvalidation';
	import LearningUnitItem from '$lib/components/learningkit/displays/LearningUnitItem.svelte';
	import { Plus } from 'lucide-svelte';
	import { INSTRUCTOR_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';

	const invalidate = useQueryInvalidation();

	interface LearningUnitsProps {
		learningKitId: string;
		learningUnits: {
			name: string;
			description?: string;
			uuid: string;
		}[];
		role: RoleType;
	}

	const { learningKitId, learningUnits, role }: LearningUnitsProps = $props();

	const deleteLearningUnitMutation = createMutation({
		mutationFn: (id: string) => api(fetch).req(deleteLearningUnit, null, id).parse(),
		onSuccess: () => {
			invalidate(['learning-kit', learningKitId]);
		}
	});
</script>

<div class="flex w-full flex-col gap-4 p-4">
	<div class="flex w-full justify-between gap-4">
		<div>
			<h2 class="preset-typo-subtitle">{$_('learningKit.learningUnits')}</h2>
			<p>{$_('learningKit.learningUnit.description')}</p>
		</div>
		{#if role === INSTRUCTOR_ROLE}
			<a
				class="btn preset-outlined-surface-500 h-fit"
				href={`/learningunit/create-form?learningKitId=${learningKitId}`}
			>
				<Plus size={24} />
				{$_('learningUnit.create')}
			</a>
		{/if}
	</div>
	<div class="grid gap-1">
		{#each learningUnits ?? [] as learningUnit (learningUnit.uuid)}
			<LearningUnitItem
				{learningUnit}
				onDeleteLearningUnit={() => {
					$deleteLearningUnitMutation.mutate(learningUnit.uuid);
				}}
				{role}
			/>
		{/each}
	</div>
</div>
