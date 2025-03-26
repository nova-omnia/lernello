<!-- +page.svelte -->
<script lang="ts">
  import { PreRendered } from 'carta-md';
  export let data: { html: string };
</script>

<div class="theory-block prose p-4 rounded-lg shadow bg-gray-500">
  <PreRendered html={data.html} />
</div>

<style>
  :global(.carta-font-code) {
    font-family: 'Fira Code', monospace;
    font-size: 1rem;
    line-height: 1.5rem;
  }
</style>
