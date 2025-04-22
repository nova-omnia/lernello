<script lang="ts">
	import { FileUpload } from '@skeletonlabs/skeleton-svelte';
	// Icons

	import { _ } from 'svelte-i18n';
	import { FilePlus } from 'lucide-svelte';

</script>

<div>
	<FileUpload
		name="Multimedia"
		accept="audio/*, video/*, image/*"
		maxFiles={1}
		onFileChange={console.log}
		onFileReject={console.error}
		classes="w-full"
		label={$_('block.multimediaBlock.label')}

	>
        {#snippet iconInterface()}<FilePlus class="size-8" />{/snippet}
	</FileUpload>
</div>
