<!-- +page.svelte -->
<script lang="ts">
  import { MarkdownEditor } from 'carta-md';
  import { carta } from '$lib/carta';
  export let data: { markdown: string; html: string };
</script>


<div class="min-h-screen w-full flex items-center justify-center">
  <div class="w-[1000px] h-[600px] border rounded p-4 mx-auto">
    <MarkdownEditor bind:value={data.markdown} mode="tabs" {carta} />
  </div>
</div>

<style>
  :global(.carta-toolbar-left button) {
    margin-right: 0.6rem;
  }
  :global(.carta-toolbar-right button) {
    margin-left: 0.2rem;
  }

  :global(.carta-editor) {
    width: 100%;
    height: 100%;
  }

  :global(.carta-wrapper) {
    width: 100%;
    height: 100%;
    background-color: white;
  }

  :global(.carta-container) {
    width: 100%;
    height: 100%;
    background-color: red;
  }

  :global(.carta-font-code) {
    width: 100%;
    height: 100%;
    background-color: green;
  }

</style>