<script lang="ts">
    import SuperDebug, {superForm, superValidate} from 'sveltekit-superforms';
    import { toaster } from '$lib/states/toasterState.svelte.js';

    let { data } = $props();
    const { form, errors, constraints, enhance } = superForm(data.form, {
        onError: (error) => {
            console.error('Error:', error.result.error);
            toaster.create({
                title: 'Error',
                description: `Uh oh, something went wrong. (${error.result.status})`,
                type: 'error'
            });
        }
    });

</script>

<form method="POST" use:enhance class="mx-auto max-w-lg space-y-4">
    <h1 class="h1">Add a Trainee</h1>

    <label class="label">
        <span class="label-text">Surname</span>
        <input
                id="surname"
                type="text"
                class="input"
                name="surname"
                placeholder="Surname of Trainee"
                aria-invalid={$errors.surname ? 'true' : undefined}
                bind:value={$form.surname}
                {...$constraints.surname}
        />
        {#if $errors.surname}
            <p class="text-error-500 text-sm">{$errors.surname}</p>
        {/if}
    </label>

    <label class="label">
        <span class="label-text">Name</span>
        <input
                id="name"
                type="text"
                class="input"
                name="name"
                placeholder="Name of Trainee"
                aria-invalid={$errors.name ? 'true' : undefined}
                bind:value={$form.name}
                {...$constraints.name}
        />
        {#if $errors.name}
            <p class="text-error-500 text-sm">{$errors.name}</p>
        {/if}
    </label>

    <label class="label">
        <span class="label-text">Username</span>
        <input
                id="username"
                type="text"
                class="input"
                name="username"
                placeholder="Username of Trainee"
                aria-invalid={$errors.username ? 'true' : undefined}
                bind:value={$form.username}
                {...$constraints.username}
        />
        {#if $errors.username}
            <p class="text-error-500 text-sm">{$errors.username}</p>
        {/if}
    </label>

    <button class="btn preset-filled-primary-400-600 w-full">Create Trainee</button>
    <SuperDebug data={$form} />
</form>