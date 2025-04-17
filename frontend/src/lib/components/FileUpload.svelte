<script lang="ts">
	import { FileUpload } from '@skeletonlabs/skeleton-svelte';
	import { Upload as IconUpload, XCircle as IconRemove } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';

</script>

<div
	class="preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center justify-center rounded-lg border-[1px] p-3 text-base"
>
	<FileUpload
		classes="w-full"
		accept=".pdf,.jpg,.jpeg,.png,.mp3,.mp4"
		subtext="Allowed file types: pdf, jpg, png, mp3, mp4"
		onFileChange={console.log}
		onFileReject={console.error}
		maxFiles={1024 * 1024 * 10}
	>
		<button class=" m-auto flex gap-3 p-2 font-semibold text-surface-900-100 justify-center ">
			<IconUpload class="size-7" />
			<p>{$_('addNewFile')}</p>
		</button>
	</FileUpload>
</div>
