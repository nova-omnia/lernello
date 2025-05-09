<script lang="ts">
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getLearningKits } from '$lib/api/collections/learningKit';
	import { getLearningKitProgressForAllParticipants } from '$lib/api/collections/progress';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import { _ } from 'svelte-i18n';
	import { Progress } from '@skeletonlabs/skeleton-svelte';
	import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';
	import type { LearningKitProgressRes } from '$lib/schemas/response/progress/LearningKitProgressResSchema';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import PlaceholderLearningKit from '$lib/components/learningkit/PlaceholderLearningKit.svelte';
	import { INSTRUCTOR_ROLE } from '$lib/schemas/response/UserInfo';
	import { goto } from '$app/navigation';
	import LearningKitStatisticCard from '$lib/components/statistics/LearningKitStatisticCard.svelte';
	import LearningKitStatistics from '$lib/components/statistics/LearningKitStatistics.svelte';

	const { data } = $props();

	if (data.role !== INSTRUCTOR_ROLE) {
		goto('/dashboard');
	}
</script>

<PageContainer title={$_('statistics.overview.title')}>
	<LearningKitStatistics />
</PageContainer>
