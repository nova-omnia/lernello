<script lang="ts">
	import { INSTRUCTOR_ROLE, type RoleType } from '$lib/schemas/response/UserInfo';
	import { _ } from 'svelte-i18n';
	import type { Snippet } from 'svelte';

	const Tab = {
		EDIT: 'edit',
		PREVIEW: 'preview'
	} as const;

	type Tab = (typeof Tab)[keyof typeof Tab];

	interface EditPreviewTabContainerProps {
		role: RoleType;
		editHeaderContent?: Snippet | undefined;
		edit: Snippet;
		preview: Snippet;
	}

	let { role, editHeaderContent, edit, preview }: EditPreviewTabContainerProps = $props();

	let activeTab = $state<Tab>(Tab.EDIT);

	let showTabs = $derived(role === INSTRUCTOR_ROLE);
</script>

<div class="flex h-full flex-col rounded-lg bg-white dark:bg-gray-800">
	{#if showTabs}
		<div
			class="flex items-center justify-between border-b border-gray-200 p-4 pb-0 dark:border-gray-700"
		>
			<div class="flex">
				<button
					type="button"
					class={`inline-block border-b-2 px-4 pt-4 pb-3 text-sm font-medium ${
						activeTab === Tab.EDIT
							? 'border-primary-500 text-primary-500'
							: 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-600 dark:text-gray-400 dark:hover:border-gray-700 dark:hover:text-gray-300'
					}`}
					onclick={() => (activeTab = Tab.EDIT)}
				>
					{$_('common.edit')}
				</button>
				<button
					type="button"
					class={`ml-2 inline-block border-b-2 px-4 pt-4 pb-3 text-sm font-medium ${
						activeTab === Tab.PREVIEW
							? 'border-primary-500 text-primary-500'
							: 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-600 dark:text-gray-400 dark:hover:border-gray-700 dark:hover:text-gray-300'
					}`}
					onclick={() => (activeTab = Tab.PREVIEW)}
				>
					{$_('common.preview')}
				</button>
			</div>

			<div class="flex items-center">
				{#if activeTab === Tab.EDIT && editHeaderContent}
					{@render editHeaderContent()}
				{/if}
			</div>
		</div>
	{/if}

	<div class="content-area flex-grow overflow-auto p-4">
		{#if !showTabs || activeTab === Tab.PREVIEW}
			{@render preview()}
		{:else if activeTab === Tab.EDIT}
			{@render edit()}
		{/if}
	</div>
</div>
