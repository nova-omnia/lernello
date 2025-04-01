<script lang="ts">
	import TheoryBlockComponent from './theoryBlockComponent.svelte';
	import QuizBlockComponent from './quizBlockComponent.svelte';
	import { BookOpen, FileText, X } from 'lucide-svelte';
	import type { Block } from '$lib/models/globalBlock';
	import {deleteBlock} from "$lib/api/learningUnits";
	import type {ToastContext} from "@skeletonlabs/skeleton-svelte";
	import {getContext} from "svelte";

	const toast: ToastContext = getContext('toast');

	export let block: Block;
	export let unitId: string;

	let Component = block.type === 'theory' ? TheoryBlockComponent : QuizBlockComponent;
	let IconComponent = block.type === 'theory' ? BookOpen : FileText;


	async function handleSubmit(event: SubmitEvent) {
		event.preventDefault();
		try {
			await deleteBlock(unitId, block.uuid);
		} catch (error) {
			toast.create({
				title: 'Error',
				description: 'Failed to delete block',
				type: 'error'
			});
		}
		toast.create({
			title: 'Success',
			description: 'Block deleted successfully',
			type: 'success'
		});
	}
</script>

<div
	class="group hover:border-primary-500 relative rounded-lg border border-gray-200 bg-white p-4 transition-all duration-200"
>
	<form onsubmit={handleSubmit} class="hidden">
		<button
			type="submit"
			class="absolute top-[-8px] right-[-8px] rounded-full bg-red-500 p-1 text-white opacity-0 shadow transition-opacity group-hover:opacity-100 hover:bg-red-600"
		>
			<X class="h-5 w-5" />
		</button>
	</form>

	<!-- Block Header -->
	<div class="flex items-center space-x-2 pl-6">
		<IconComponent class="h-6 w-6 text-gray-500" />
		<h3 class="font-medium">{block.name}</h3>
		<span class="ml-2 text-sm text-gray-500">({block.type})</span>
	</div>
	<!-- Block Content -->
	<svelte:component this={Component} />
</div>
