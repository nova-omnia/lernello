<script lang="ts">
    import { page } from '$app/state';
    import SuperDebug, { superForm } from 'sveltekit-superforms';
    import { getContext } from 'svelte';
    import { type ToastContext } from '@skeletonlabs/skeleton-svelte';
    import { _ } from 'svelte-i18n';

    const toast: ToastContext = getContext('toast');

    let { data } = $props();

    const { form, errors, constraints, message, enhance } = superForm(data.form, {
        onError: (error) => {
            console.error('Error:', error.result.error);
            toast.create({
                title: $_('error.title'),
                description: $_('error.description', { values: { status: error.result.status } }),
                type: 'error'
            });
        }
    });
</script>

<main class="flex h-full flex-col items-center justify-center">
    {#if $message}<h3>{$message}</h3>{/if}

    <form
        method="POST"
        use:enhance
        action="{page.url.search ?? ''}{page.url.search ? '&' : '?'}/login"
        class="card preset-filled-surface-100-900 border-surface-200-800 w-full max-w-lg space-y-8 border-[1px] p-8"
    >
        <h1 class="h2">{$_('login.title')}</h1>

        <div class="space-y-4">
            <label class="label">
                <span class="label-text">{$_('form.emailLabel')}</span>
                <input
                    class="input preset-filled-surface-200-800"
                    name="username"
                    type="text"
                    placeholder={$_('form.emailPlaceholder')}
                    aria-invalid={$errors.username ? 'true' : undefined}
                    bind:value={$form.username}
                    {...$constraints.username}
                />
                {#if $errors.username}<span class="text-error-50-950">{$errors.username}</span>{/if}
            </label>
            <label class="label">
                <span class="label-text">{$_('form.passwordLabel')}</span>
                <input
                    class="input preset-filled-surface-200-800"
                    name="password"
                    type="password"
                    placeholder={$_('form.passwordPlaceholder')}
                    aria-invalid={$errors.password ? 'true' : undefined}
                    bind:value={$form.password}
                    {...$constraints.password}
                />
                {#if $errors.password}<span class="text-error-50-950">{$errors.password}</span>{/if}
            </label>
        </div>
        <button class="btn preset-filled-primary-500 w-full">{$_('login.signIn')}</button>
    </form>
    <SuperDebug data={$form} />
</main>
