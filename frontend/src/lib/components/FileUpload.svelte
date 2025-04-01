<script lang="ts">
	import { FileUpload } from '@skeletonlabs/skeleton-svelte';
	import {
		Upload as IconUpload,
		Paperclip as IconFile,
		XCircle as IconRemove
	} from 'lucide-svelte';
</script>

<FileUpload
	name="file-upload"
	accept="image/*,text/*,.html,.htm,application/*"
	subtext="Attach up to 100 files."
	maxFileSize={1024 * 1024 * 10}
	maxFiles={100}
	onFileChange={console.log}
	onFileReject={console.error}
	allowDrop
	classes="w-full"
>
	{#snippet iconInterface()}<IconUpload class="size-8" />{/snippet}
	{#snippet iconFile()}<IconFile class="size-4" />{/snippet}
	{#snippet iconFileRemove()}<IconRemove class="size-4" />{/snippet}
</FileUpload>
