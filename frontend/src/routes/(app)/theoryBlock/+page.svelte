  <script lang="ts">
	import { redirect } from '@sveltejs/kit';
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

  :global(.carta-toolbar) {
    padding-bottom: 1rem;
  }
  :global(.carta-toolbar-left button) {
    margin-right: 1rem;
  }
  :global(.carta-toolbar-right button) {
    margin-left: 0.5rem;
  }

  :global(.carta-editor) {
    width: 100%;
    height: 100%;
  }

  :global(.carta-renderer) {
    width: 100%;
    height: 100%; 
    background-color: black;

  }

  :global(.carta-font-code) {
    width: 100%;
    height: 100%;
    background-color: white;
  }

  :global(-carta-input-wrapper) {
    width: 100%;
    height: 100%;
    background-color: red;
  }

  :global(.carta-input) {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
  }

  :global(.carta-input textarea) {
    flex: 1;
    width: 100%;
    height: 100%;
    resize: none; /* Optional: Prevent resizing if not YY Y needed */
  }
</style>