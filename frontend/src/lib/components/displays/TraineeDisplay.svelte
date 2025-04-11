<script lang="ts">
    import { Avatar } from '@skeletonlabs/skeleton-svelte';

    const {User} = $props();
</script>
    <div
            class="Trainee-Display preset-filled-surface-100-900 rounded-border border-surface-200-800 flex items-center rounded-lg border-[1px] p-3 text-base w-full"
    >
            <Avatar
                    name="{User.surname} {User.name}"
                    src={User.avatar}
            />

            <p class="font-bold text-black-700 text-base ml-2"> {User.name} </p>


        <button type="button" class="btn preset-outlined-surface-500 bg-gray ml-auto rounded-full p-2">Invite</button>
        <button type="button" class="btn preset-filled-error-500 ml-1 rounded-full p-2">Remove</button>
    </div>
