<!-- +page.svelte -->
<script lang="ts">
  import { MarkdownEditor, PreRendered } from 'carta-md';
  import { carta } from '$lib/carta'; // Import the carta instance
  export let data: { markdown: string; html: string };
</script>

<div class="theory-block prose p-4 rounded-lg shadow bg-gray-500">
  <!-- Pass the imported carta instance -->
</div>

<style>
  :global(.carta-font-code) {
    font-family: 'Fira Code', monospace;
    font-size: 1rem;
    line-height: 1.5rem;
  }
</style>

<MarkdownEditor bind:value={data.markdown} mode="tabs" theme="github" {carta} />