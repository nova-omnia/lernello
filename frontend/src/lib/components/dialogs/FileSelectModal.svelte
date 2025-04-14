<script lang="ts">
	import { browserApiClient } from '$lib/api/browserApiClient';
	import { getAllFiles } from '$lib/api/collections/file';
	import type { FileRes } from '$lib/schemas/response/FileRes';
	import { Modal } from '@skeletonlabs/skeleton-svelte';
	import { _ } from 'svelte-i18n';

	interface FileSelectModalProps {
		isOpen: boolean;
		onSelect: (uuids: string[]) => void;
		onClose: () => void;
	}

	const { isOpen, onSelect, onClose }: FileSelectModalProps = $props();

	let availableFiles = $state<FileRes[]>([]);
	let selectedFiles = $state<string[]>([]);

	$effect(() => {
		if (isOpen) {
			async function asyncWrapper() {
				const data = await browserApiClient.req(getAllFiles, null);
				// availableFiles = [
				// 	{ uuid: '1', name: 'File 1' },
				// 	{ uuid: '2', name: 'File 2' },
				// 	{ uuid: '3', name: 'File 3' },
				// 	{ uuid: '4', name: 'File 4' },
				// 	{ uuid: '5', name: 'File 5' },
				// 	{ uuid: '6', name: 'File 6' },
				// 	{ uuid: '7', name: 'File 7' },
				// 	{ uuid: '8', name: 'File 8' },
				// 	{ uuid: '9', name: 'File 9' },
				// 	{ uuid: '10', name: 'File 10' },
				// 	{ uuid: '11', name: 'File 11' },
				// 	{ uuid: '12', name: 'File 12' },
				// 	{ uuid: '13', name: 'File 13' },
				// 	{ uuid: '14', name: 'File 14' },
				// 	{ uuid: '15', name: 'File 15' }
				// ];
				availableFiles = data;
			}
			asyncWrapper();
		}
	});
</script>

<Modal
	open={isOpen}
	contentBase="card bg-surface-100-900 p-4 space-y-4 shadow-xl max-w-screen-sm"
	backdropClasses="backdrop-blur-sm"
>
	{#snippet content()}
		<div class="bg-opacity-50 fixed inset-0 z-50 flex items-center justify-center">
			<div class="w-full max-w-3xl rounded p-6 shadow-xl">
				<h2 class="mb-4 text-lg font-bold">{$_('selectFiles')}</h2>

				<div class="max-h-64 overflow-auto">
					<table class="table w-full">
						<thead>
							<tr>
								<th>{$_('multiSelect.select')}</th>
								<th>{$_('multiSelect.name')}</th>
							</tr>
						</thead>
						<tbody>
							{#each availableFiles as file (file.uuid)}
								<tr>
									<td>
										<input type="checkbox" bind:group={selectedFiles} value={file.uuid} />
									</td>
									<td>{file.name}</td>
								</tr>
							{/each}
						</tbody>
					</table>
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
