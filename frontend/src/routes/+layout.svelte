<script lang="ts">
    import '../app.css';
    import { ToastProvider } from '@skeletonlabs/skeleton-svelte';
    import { i18nReady } from '$lib/i18n/i18n';
    import LoadingRing from '$lib/components/LoadingRing.svelte';

    let ready = false;

    i18nReady.then(() => {
        ready = true;
    });
</script>

{#if !ready}
    <LoadingRing />
{:else}
    <ToastProvider>
        <slot />
    </ToastProvider>
{/if}
