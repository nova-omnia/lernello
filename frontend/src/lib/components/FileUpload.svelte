<script lang="ts">
	import { api } from '$lib/api/apiClient';
	import { uploadFile } from '$lib/api/collections/file';
	import { FileUpload } from '@skeletonlabs/skeleton-svelte';
	import { Upload as IconUpload, XCircle as IconRemove } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';

	let name = $state<string>('');

	//JOSI: TODO
	async function handleAddFiles() {
		const createdFile = { name };
		await api(fetch).req(uploadFile, createdFile).parse();
	}
</script>

<div
	class="preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center justify-center rounded-lg border-[1px] p-3 text-base"
>
	<FileUpload
		classes="w-full"
		accept=".pdf,.jpg,.jpeg,.png,.mp3,.mp4"
		subtext="Allowed file types: pdf, jpg, png, mp3, mp4"
		onFileChange={handleAddFiles}
		onFileReject={console.error}
		maxFiles={1024 * 1024 * 10}
	>
		<button class=" text-surface-900-100 m-auto flex justify-center gap-3 p-2 font-semibold">
			<IconUpload class="size-7" />
			<p>{$_('addNewFile')}</p>
		</button>
	</FileUpload>
</div>
