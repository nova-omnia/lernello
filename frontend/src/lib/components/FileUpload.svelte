<script lang="ts">
	import { FileUpload } from '@skeletonlabs/skeleton-svelte';
	import { Upload as IconUpload, File as IconFile, XCircle as IconRemove } from 'lucide-svelte';
</script>

<FileUpload name="example-button" accept="image/*" onFileChange={console.log} maxFiles={2}>
	<button class="btn preset-filled">
		<IconUpload class="size-4" />
		<span>Select File</span>
	</button>
</FileUpload>
