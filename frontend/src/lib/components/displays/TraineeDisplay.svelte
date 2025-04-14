<script lang="ts">
	import { Avatar } from '@skeletonlabs/skeleton-svelte';
	const { user } = $props();
</script>

<div
	class="preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center rounded-lg border-[1px] p-3 text-base"
>
	<Avatar name="{user.surname} {user.name}" classes="h-11 w-11" />

	<p class="text-black-700 ml-3 text-xs font-bold">{user.surname} {user.name}</p>
	<!-- todo> -->
	<!-- <button type="button" class="btn preset-outlined-surface-500 bg-gray ml-auto rounded-full p-2"
		>{$_('button.edit')}</button
	>	 
	<button
		type="button"
		class="btn preset-filled-error-500 ml-auto rounded-full p-2"
		onclick={(e) => {
			e.preventDefault();
			showDeleteDialog = true;
		}}>{$_('button.remove')}</button
	>-->
</div>
