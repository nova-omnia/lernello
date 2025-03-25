<!-- src/routes/(app)/theoryBlock/+page.svelte -->
<script lang="ts">
  import { unified } from 'unified';
  import remarkParse from 'remark-parse';
  import remarkMath from 'remark-math';
  import remarkRehype from 'remark-rehype';
  import rehypeKatex from 'rehype-katex';
  import rehypeRaw from 'rehype-raw';
  import rehypeStringify from 'rehype-stringify';
  import 'katex/dist/katex.min.css';

  export let data;
  let input = data?.content ?? '# Kein Inhalt geladen';
  let rendered: string = '';
  let activeView: 'edit' | 'preview' = 'edit';

  const preserveTabsAndSpaces = (text: string) => {
    return text
      .replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;') // Tabs in 4 non-breaking spaces umwandeln
      .replace(/^ +/gm, (match) => match.split('').map(() => '&nbsp;').join('')); // Führende Leerzeichen bewahren
  };

  const handleTab = (event: KeyboardEvent) => {
    if (event.key === 'Tab') {
      event.preventDefault(); // Verhindert das Standardverhalten (Wechsel des Fokus)
      const textarea = event.target as HTMLTextAreaElement;
      const start = textarea.selectionStart;
      const end = textarea.selectionEnd;

      // Füge ein Tab-Zeichen ein
      textarea.value =
        textarea.value.substring(0, start) + '\t' + textarea.value.substring(end);

      // Setze den Cursor hinter das Tab-Zeichen
      textarea.selectionStart = textarea.selectionEnd = start + 1;

      // Aktualisiere den gebundenen Wert
      input = textarea.value;
    }
  };

  $: if (input) {
    unified()
      .use(remarkParse)
      .use(remarkMath)
      .use(remarkRehype, { allowDangerousHtml: true })
      .use(rehypeRaw)
      .use(rehypeKatex)
      .use(rehypeStringify)
      .process(preserveTabsAndSpaces(input)) // Tabs und führende Leerzeichen bewahren
      .then((file) => {
        rendered = String(file);
      });
  }
</script>

<!-- Vollbild-Container -->
<div class="w-screen h-screen flex flex-col">
  <!-- Topbar -->
  <div class="flex justify-center space-x-4 p-4 flex-none">
    <button 
      on:click={() => activeView = 'edit'} 
      class="btn {activeView === 'edit' ? 'btn-primary' : 'btn-secondary'}">
      EDIT
    </button>
    <button 
      on:click={() => activeView = 'preview'} 
      class="btn {activeView === 'preview' ? 'btn-primary' : 'btn-secondary'}">
      PREVIEW
    </button>
  </div>

  <!-- Content-Bereich -->
  <div class="flex-1 overflow-hidden p-4">
    {#if activeView === 'edit'}
      <textarea
        class="textarea bg-base-200 p-4 rounded-lg w-full h-full resize-none"
        bind:value={input}
        on:keydown={handleTab}>
      </textarea>
    {:else if activeView === 'preview'}
      <div
        class="bg-white text-black p-4 rounded shadow overflow-auto w-full h-full prose prose-sm lg:prose-lg"
        style="white-space: pre-wrap;">
        {@html rendered}
      </div>
    {/if}
  </div>
</div>
