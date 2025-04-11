<script lang="ts">
	const { files, open, onSelect, onClose } = $props<{
		files: { uuid: string; name: string }[];
		open: boolean;
		onSelect: (uuids: string[]) => void;
		onClose: () => void;
	}>();
	let selectedFiles = $state<string[]>([]);

	function addFiles() {
		onSelect?.(selectedFiles);
		selectedFiles = [];
	}

	function closeModal() {
		onClose?.();
		selectedFiles = [];
	}
</script>

{#if open}
	<div class="bg-opacity-50 fixed inset-0 z-50 flex items-center justify-center bg-black">
		<div class="w-full max-w-3xl rounded bg-white p-6 shadow-xl">
			<h2 class="mb-4 text-lg font-bold">Select Files</h2>

			<div class="max-h-64 overflow-auto">
				<table class="table w-full">
					<thead>
						<tr>
							<th>Select</th>
							<th>Name</th>
						</tr>
					</thead>
					<tbody>
						{#each files as file (file.uuid)}
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
				<button class="btn" onclick={closeModal}>Cancel</button>
				<button class="btn btn-primary" onclick={addFiles}>Add Selected</button>
			</div>
		</div>
	</div>
{/if}
