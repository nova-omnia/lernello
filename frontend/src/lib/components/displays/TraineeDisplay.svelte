<script lang="ts">
	import { Avatar } from '@skeletonlabs/skeleton-svelte';
	const { User } = $props();

	function removeTrainee(event: MouseEvent & { currentTarget: EventTarget & HTMLButtonElement; }) {
		//todo
	}
</script>

<div
	class="Trainee-Display preset-filled-surface-100-900 rounded-border border-surface-200-800 flex w-full items-center rounded-lg border-[1px] p-3 text-base"
>
	<Avatar name="{User.surname} {User.name}" src={User.avatar} classes="h-11 w-11" />

	<p class="text-black-700 ml-3 text-xs font-bold">{User.surname} {User.name}</p>

	<button type="button" class="btn preset-outlined-surface-500 bg-gray ml-auto rounded-full p-2"
		>Invite</button
	>
	<button
		type="button"
		class="btn preset-filled-error-500 ml-1 rounded-full p-2"
		onclick={removeTrainee}>Remove</button
	>
</div>
