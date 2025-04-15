<script lang="ts">
	import { browserApiClient } from '$lib/api/browserApiClient';
	import { getAllFiles } from '$lib/api/collections/file';
	import type { FileRes } from '$lib/schemas/response/FileRes';
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';
	import { writable, derived } from 'svelte/store';

	interface FileSelectModalProps {
		isOpen: boolean;
		onSelect: (uuids: string[]) => void;
		onClose: () => void;
	}

	const { isOpen, onSelect, onClose }: FileSelectModalProps = $props();

	let availableFiles = $state<FileRes[]>([]);
	let selectedFiles = $state<string[]>([]);
	let searchValue = writable(''); //TODO: dont use store
	let loading = writable(true); //TODO: dont use store

	const filteredFiles = derived([searchValue, loading], ([$searchValue, $loading]) => {
		if ($loading) return [];
		return $searchValue
			? availableFiles.filter((file) =>
					file.name.toLowerCase().includes($searchValue.toLowerCase())
				)
			: availableFiles;
	});

	$effect(() => {
		if (isOpen) {
			async function fetchFiles() {
				loading.set(true);
				try {
					const data = await browserApiClient.req(getAllFiles, null);
					availableFiles = data;
				} catch (error) {
					console.error('Error fetching files:', error);
				} finally {
					loading.set(false);
				}
			}
			fetchFiles();
		}
	});
</script>

<Modal
	open={isOpen}
	contentBase="card bg-surface-200-800 shadow-lg max-w-screen-sm"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<div class="bg-opacity-50 fixed inset-0 z-50 flex items-center justify-center">
			<div class="w-full max-w-3xl rounded p-6 shadow-xl">
				<h2 class="mb-4 text-lg font-bold">{$_('selectFiles')}</h2>

				<input
					type="text"
					placeholder={$_('multiSelect.searchPlaceholder')}
					bind:value={$searchValue}
					class="bg-surface-200-800 text-surface-800-200 w-full px-3 py-2"
					disabled={$loading}
				/>

				<div class="max-h-64 min-h-70 overflow-auto">
					{#if $loading}
						<p class="text-center">{$_('loading')}</p>
					{:else}
						<table class="table w-full">
							<thead>
								<tr>
									<th>{$_('multiSelect.select')}</th>
									<th>{$_('multiSelect.name')}</th>
								</tr>
							</thead>
							<tbody>
								{#each $filteredFiles as file (file.uuid)}
									<tr>
										<td>
											<input type="checkbox" bind:group={selectedFiles} value={file.uuid} />
										</td>
										<td>{file.name}</td>
									</tr>
								{/each}
							</tbody>
						</table>
					{/if}
				</div>

				<div class="mt-4 flex justify-end gap-2">
					<button
						class="btn"
						onclick={() => {
							onClose();
							selectedFiles = [];
						}}>{$_('button.cancel')}</button
					>
					<button
						class="btn btn-primary"
						onclick={() => {
							onSelect(selectedFiles);
							selectedFiles = [];
						}}>{$_('button.addSelected')}</button
					>
				</div>
			</div>
		</div>
	{/snippet}
</Modal>
