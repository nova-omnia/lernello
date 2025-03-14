<script lang="ts">
    import '../app.css';
	import './nav.css';
	import './dashboard.css';
    let { children } = $props();
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

<div>
	<nav>
		<div>
			<a href="/">Dashboard</a>
			<a href="/learningkit">Learning Kits</a>
		</div>
		<form method="POST" class="search-bar" onsubmit={handleSearch}>
			<input type="text" bind:value={searchQuery} placeholder="Search..." >
		</form>
	</nav>

	{@render children()}
</div>