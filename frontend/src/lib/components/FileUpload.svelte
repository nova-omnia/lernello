<script lang="ts">
	import { FileUpload } from '@skeletonlabs/skeleton-svelte';
	import { Upload as IconUpload } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';

	// JOSI: TODO
	// async function handleUploadFile() {
	// 	const createdFile = { name };
	// 	await api(fetch).req(uploadFile, createdFile).parse();
	// }
</script>

<button type="button" class="btn preset-outlined-surface-500 h-fit">
	<FileUpload
		classes="w-full"
		accept=".pdf,.jpg,.jpeg,.png,.mp3,.mp4"
		onFileChange={console.log}
		onFileReject={console.error}
		maxFiles={1024 * 1024 * 10}
	>
		<button class=" text-surface-900-100 font- m-auto flex justify-center gap-3">
			<IconUpload size={24} />
			{$_('addNewFile')}
		</button>
	</FileUpload>
</button>
