<script lang="ts">
	import {isExpanded} from '$lib/stores/sidebar';
	import {Sidebar} from 'lucide-svelte';

	function toggleSidebar() {
		isExpanded.update((value) => !value);
	}
	let searchQuery = '';
	async function handleSearch(event: Event) {
		event.preventDefault();
		const response = await fetch('/search', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({ query: searchQuery })
		});
		if (!response.ok) {
			console.error('Search request failed:', response.statusText);
			return;
		}
		const result = await response.json();
		console.log('Search results:', result);
		// Handle the search results here
	}
</script>

<nav class=" pl-2 flex preset-filled-primary-100-900 ">
	<button
		class="flex h-12 w-12 items-center justify-center rounded"
		onclick={toggleSidebar}
		aria-label="Toggle sidebar"
		aria-expanded={$isExpanded}
		class:rotate-180={$isExpanded}
	>
		<Sidebar size={24} />
	</button>
	<div class="input-group grid-cols-[auto_1fr_auto] items-center justify-center disabled">
		<form method="POST" onsubmit={handleSearch}>
			<input class="ig-input preset-tonal rounded" type="search" bind:value={searchQuery} placeholder="Search..." />
			<button class="ig-btn preset-filled-primary-500 rounded">Submit</button>
		</form>
	</div>
</nav>
