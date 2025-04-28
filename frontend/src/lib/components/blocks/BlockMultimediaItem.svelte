<script lang="ts">
	import { FileUpload } from '@skeletonlabs/skeleton-svelte';
	import { FilePlus } from 'lucide-svelte';
	import { _ } from 'svelte-i18n';
	import { onDestroy } from 'svelte';
	import { FileRejection } from '@zag-js/file-upload';

	let previewUrl: string | null = null;
	let fileType: 'image' | 'pdf' | 'video' | 'audio' | 'other' | null = null;
	let imageName: string | null = null;
	let currentObjectUrl: string | null = null; // For revokeObjectURL
	let fileUploaded: boolean = false; // New state variable

	type FileChangeDetails = {
		acceptedFiles: File[];
		rejectedFiles: FileRejection[];
	};

	function cleanupObjectUrl() {
		if (currentObjectUrl) {
			URL.revokeObjectURL(currentObjectUrl);
			currentObjectUrl = null;
		}
	}

	onDestroy(() => {
		cleanupObjectUrl();
	});

	function handleFileChange(details: FileChangeDetails) {
		console.log('FileUpload Details:', details);

		cleanupObjectUrl();
		previewUrl = null;
		fileType = null;
		imageName = null;

		const files = details.acceptedFiles;

		if (files && files.length > 0) {
			fileUploaded = true;

			const file = files[0];
			imageName = file.name;
			const mimeType = file.type;

			if (mimeType.startsWith('image/')) {
				fileType = 'image';
				const reader = new FileReader();
				reader.onload = (e: ProgressEvent<FileReader>) => {
					previewUrl = e.target?.result as string;
				};
				reader.onerror = () => {
					alert($_('block.multimediaBlock.errorReadingImage'));
					fileType = null;
				};
				reader.readAsDataURL(file);
			} else if (mimeType === 'application/pdf') {
				fileType = 'pdf';
				currentObjectUrl = URL.createObjectURL(file);
				previewUrl = currentObjectUrl;
			} else if (mimeType.startsWith('video/')) {
				fileType = 'video';
				currentObjectUrl = URL.createObjectURL(file);
				previewUrl = currentObjectUrl;
			} else if (mimeType.startsWith('audio/')) {
				fileType = 'audio';
				currentObjectUrl = URL.createObjectURL(file);
				previewUrl = currentObjectUrl;
			} else {
				fileType = 'other';
				previewUrl = null;
			}
		} else {
			cleanupObjectUrl();
			previewUrl = null;
			fileType = null;
			imageName = null;
			fileUploaded = false;
		}
	}
</script>

<div
	class="preset-filled-primary-100-900 rounded-border border-surface-200-800 flex w-full items-center justify-center rounded-lg border-[1px] p-2 text-center text-base"
>
	{#if !fileUploaded}
		<FileUpload
			classes="w-full"
			accept=".pdf,.jpg,.jpeg,.png,.mp3,.mp4"
			onFileChange={handleFileChange}
			onFileReject={console.error}
			maxFiles={1024 * 1024 * 10}
			label={$_('block.multimediaBlock.label')}
		>
			{#snippet iconInterface()}<FilePlus />
			{/snippet}
		</FileUpload>
	{/if}
	<div class="border-surface-200-700 mt-4 min-h-[50px] border-t pt-4">
		{#if fileType}
			<h3 class="h3 text-surface-900-50 mb-2">{$_('common.preview')}</h3>
		{/if}

		{#if previewUrl}
			{#if fileType === 'image'}
				<img
					src={previewUrl}
					alt={$_('block.multimediaBlock.imageAlt')}
					class="border-surface-300-600 h-auto max-w-full rounded border"
				/>
			{:else if fileType === 'pdf'}
				<embed
					src={previewUrl}
					type="application/pdf"
					width="100%"
					height="600px"
					class="border-surface-300-600 rounded border"
				/>
			{:else if fileType === 'video'}
				<video
					controls
					src={previewUrl}
					class="border-surface-300-600 bg-surface-100-800 max-w-full rounded border"
				>
					<track kind="captions" src={previewUrl} />
					{$_('block.multimediaBlock.videoNotSupported')}</video
				>
			{:else if fileType === 'audio'}
				<audio controls src={previewUrl} class="w-full"
					>{$_('block.multimediaBlock.audioNotSupported')}</audio
				>
			{/if}
		{:else if fileType === 'other'}
			<div class="border-surface-300-600 bg-surface-50-800 rounded border p-4 text-center">
				<p class="text-surface-600-300 text-sm">
					{$_('block.multimediaBlock.noPreview', { values: { fileName: imageName } })}
				</p>
				<svg
					xmlns="http://www.w3.org/2000/svg"
					class="text-surface-400-500 mx-auto mt-2 h-12 w-12"
					fill="none"
					viewBox="0 0 24 24"
					stroke="currentColor"
					stroke-width="1"
					><path
						stroke-linecap="round"
						stroke-linejoin="round"
						d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z"
					/></svg
				>
			</div>
		{/if}

		{#if fileUploaded}
			<div class="mt-4 text-center">
				<button
					class="btn preset-filled-tertiary-500 text-sm"
					on:click={() => {
						fileUploaded = false;
						cleanupObjectUrl();
						previewUrl = null;
						fileType = null;
						imageName = null;
					}}
				>
					{$_('button.cancel')}
				</button>
			</div>
		{/if}
	</div>
</div>
