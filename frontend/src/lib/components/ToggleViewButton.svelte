<script lang="ts">
	import { goto } from '$app/navigation';
	import { page } from '$app/state';
	import { _ } from 'svelte-i18n';

	const path = page.url.pathname;
	const previewMode = path.endsWith('/training');

	function goToEditor() {
		if (previewMode) goto(path.replace(/\/training$/, ''));
	}

	function goToPreview() {
		if (!previewMode) goto(`${path}/training`);
	}
</script>

<nav class="btn-group preset-filled-surface-100-900">
	<button
		type="button"
		class={`btn ${!previewMode ? 'preset-filled-surface-300-700' : 'preset-filled-surface-100-900'}`}
		onclick={goToEditor}
	>
		{$_('common.edit')}
	</button>
	<button
		type="button"
		class={`btn ${previewMode ? 'preset-filled-surface-300-700' : 'preset-filled-surface-100-900'}`}
		onclick={goToPreview}
	>
		{$_('common.preview')}
	</button>
</nav>
