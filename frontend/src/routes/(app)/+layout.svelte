<script lang="ts">
	import Sidebar from '$lib/components/Sidebar.svelte';
	import Topbar from '$lib/components/Topbar.svelte';
	import { _ } from 'svelte-i18n';
	import { goto } from '$app/navigation';
	import { ChevronLeft } from 'lucide-svelte';

	export function goBack() {
		if (history.length > 1) {
			history.back();
		} else {
			goto('/');
		}
	}

	let { children } = $props();
</script>

<div class="flex h-full">
	<Sidebar />
	<div class="flex flex-1 flex-col">
		<Topbar />
		<main class="relative min-h-0 flex-1 overflow-auto p-4">
			<div class="px-4 pt-4">
				<button class="flex items-center gap-1" onclick={goBack}>
					<ChevronLeft size={16} />
					{$_('common.return')}
				</button>
			</div>
			{@render children()}
		</main>
	</div>
</div>
