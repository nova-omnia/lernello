<script lang="ts">
	import { _ } from 'svelte-i18n';
	import PlaceholderLearningKit from '$lib/components/learningkit/PlaceholderLearningKit.svelte';
	import AddLearningKit from '$lib/components/learningkit/AddLearningKit.svelte';
	import ErrorIllustration from '$lib/components/ErrorIllustration.svelte';
	import { createQuery } from '@tanstack/svelte-query';
	import { api } from '$lib/api/apiClient';
	import { getLearningKits } from '$lib/api/collections/learningKit';
	import PageContainer from '$lib/components/layout/PageContainer.svelte';
	import LearningKitItem from '$lib/components/learningkit/LearningKitItem.svelte';
	import { INSTRUCTOR_ROLE, TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';

	const { data } = $props();

	const kitsQuery = createQuery({
		queryKey: ['all-learning-kits-list'],
		queryFn: () => api(fetch).req(getLearningKits, null, { size: 100, page: 0 }).parse()
	});
</script>

<PageContainer title={$_('learningKits.title')}>
	<div class="container flex space-y-4">
		<div class="container flex h-36 flex-wrap gap-2">
			{#if $kitsQuery.status === 'pending'}
				{#each Array(3)}
					<PlaceholderLearningKit />
				{/each}
			{:else if $kitsQuery.status === 'error'}
				<ErrorIllustration>{$_('learningKit.error.loadList')}</ErrorIllustration>
			{:else}
				{#each $kitsQuery.data.content as kit (kit.uuid)}
					<div
						title={data.userInfo.role === TRAINEE_ROLE &&
						kit.deadlineDate &&
						new Date(kit.deadlineDate) < new Date()
							? $_('learningKit.expired')
							: ''}
					>
						<div
							class={`transition-opacity duration-300 ${
								data.userInfo.role === TRAINEE_ROLE &&
								kit.deadlineDate &&
								new Date(kit.deadlineDate) < new Date()
									? 'pointer-events-none cursor-not-allowed opacity-50'
									: ''
							}`}
						>
							<LearningKitItem
								title={kit.name}
								uuid={kit.uuid}
								role={data.userInfo.role}
								published={kit.published}
							/>
						</div>
					</div>
				{/each}
				{#if data.userInfo.role === INSTRUCTOR_ROLE}
					<AddLearningKit title={$_('learningKit.create')} />
				{/if}
			{/if}
		</div>
	</div>
</PageContainer>
