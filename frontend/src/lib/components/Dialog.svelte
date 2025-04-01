<script lang="ts">
    export let dialog
</script>
<dialog bind:this={dialog} on:close>
    <slot/>
</dialog>
