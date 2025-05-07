<script lang="ts">
	import { api } from '$lib/api/apiClient';
	import { uploadFile } from '$lib/api/collections/file';
	import type { FileRes } from '$lib/schemas/response/FileRes';
	import { toaster } from '$lib/states/toasterState.svelte';
	import { FileUpload, type FileUploadApi } from '@skeletonlabs/skeleton-svelte';
	import { createMutation } from '@tanstack/svelte-query';
	import { UploadIcon } from 'lucide-svelte';
	import { onMount } from 'svelte';
	import { _ } from 'svelte-i18n';

	interface FileUploadProps {
		onFileUploaded: (uploadedFileInfo: FileRes) => void;
	}

	let { onFileUploaded }: FileUploadProps = $props();

	const uploadFileMutation = createMutation({
		mutationFn: (data: FormData) => api(fetch).req(uploadFile, data).parse(),
		onMutate: () => {
			toaster.create({
				title: $_('files.upload.uploading'),
				description: $_('files.upload.uploading.description'),
				type: 'info'
			});
		},
		onSuccess: (uploadedFileInfo) => {
			onFileUploaded(uploadedFileInfo);
		},
		onError: (error) => {
			console.error('Error uploading file:', error);
			toaster.create({
				title: $_('common.error.title'),
				description: $_('error.description', { values: { status: 'unknown' } }),
				type: 'error'
			});
		}
	});
	let fileApi: FileUploadApi | null = $state(null);
	let isDraggingFile = $state(false);

	onMount(() => {
		let dragCounter = 0;

		function onDragEnter(event: DragEvent) {
			if (event.dataTransfer?.types?.includes('Files')) {
				dragCounter++;
				isDraggingFile = true;
			}
		}

		function onDragOver(event: DragEvent) {
			if (event.dataTransfer?.types?.includes('Files')) {
				event.preventDefault();
			}
		}

		function onDrop(event: DragEvent) {
			dragCounter = 0;
			isDraggingFile = false;
			if (event.dataTransfer?.files) {
				const files = Array.from(event.dataTransfer.files);
				if (files.length > 0) {
					fileApi?.setFiles(files);
				}
			}
		}

		function onDragLeave() {
			dragCounter--;
			if (dragCounter <= 0) {
				isDraggingFile = false;
			}
		}

		window.addEventListener('dragenter', onDragEnter);
		window.addEventListener('dragover', onDragOver);
		window.addEventListener('dragleave', onDragLeave);
		window.addEventListener('drop', onDrop);
		return () => {
			window.removeEventListener('dragenter', onDragEnter);
			window.removeEventListener('dragover', onDragOver);
			window.removeEventListener('dragleave', onDragLeave);
			window.removeEventListener('drop', onDrop);
		};
	});
</script>

<div>
	<FileUpload
		accept=".pdf,.jpg,.jpeg,.png,.mp3,.mp4"
		onFileReject={(details) => {
			details.files.forEach((file) => {
				file.errors.forEach((error) => {
					toaster.create({
						title: $_('files.upload.reject.title'),
						description: $_('files.upload.reject.description', { values: { reason: error } }),
						type: 'info'
					});
				});
			});
		}}
		onFileAccept={(details) => {
			if (!fileApi) throw new Error('FileUpload API not initialized');
			details.files.forEach((file) => {
				const formData = new FormData();
				formData.append('file', file);
				$uploadFileMutation.mutate(formData);
			});
			fileApi.clearFiles();
		}}
		onApiReady={(api) => {
			fileApi = api;
		}}
		maxFiles={1024 * 1024 * 10}
	>
		<button class="btn preset-outlined-surface-500">
			<UploadIcon size={24} />
			{$_('addNewFile')}
		</button>
	</FileUpload>
</div>

{#if isDraggingFile}
	<div class="pointer-events-none fixed inset-0 border-4 border-dashed border-blue-500"></div>
{/if}
