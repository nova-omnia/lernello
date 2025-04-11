<script lang="ts">
	import { FileUpload } from '@skeletonlabs/skeleton-svelte';
	import { Upload as IconUpload, File as IconFile, XCircle as IconRemove } from 'lucide-svelte';
</script>

<FileUpload
	name="file-upload"
	accept="image/*,text/*,.html,.htm,application/*"
	maxFileSize={1024 * 1024 * 10}
	maxFiles={100}
	onFileChange={console.log}
	onFileReject={console.error}
	allowDrop
	classes="w-full flex flex-col items-center justify-center rounded-border"
>
	{#snippet iconInterface()}<IconUpload class="size-8" />{/snippet}
	{#snippet iconFile()}<IconFile class="h-10 w-10" />{/snippet}
	{#snippet iconFileRemove()}<IconRemove class="h-5 w-5" />{/snippet}
</FileUpload>
