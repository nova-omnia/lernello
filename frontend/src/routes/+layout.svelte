<script lang="ts">
    import '../app.css';
    import {ToastProvider} from '@skeletonlabs/skeleton-svelte';
    import '$lib/i18n/i18n.ts';
    import {isLoading} from 'svelte-i18n'
    import LoadingRing from "$lib/components/LoadingRing.svelte";

    let {children} = $props();
</script>

{#if $isLoading}
    <LoadingRing/>
{:else}
    <ToastProvider>
        {@render children()}
    </ToastProvider>
{/if}
