<script lang="ts">
    import '../app.css';
    let { children } = $props();
    let searchQuery = $state('');
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
	<nav class="flex justify-between items-center p-2.5 bg-gray-100 border-b border-gray-300">
        <div>
            <a href="/" class="mr-5 text-blue-500 no-underline hover:underline">Dashboard</a>
            <a href="/learningkit" class="mr-5 text-blue-500 no-underline hover:underline">Learning Kits</a>
        </div>
        <form method="POST" class="flex items-center" onsubmit={handleSearch}>
            <input type="text" bind:value={searchQuery} placeholder="Search..." class="p-1 border border-gray-300 rounded mr-2.5 text-black bg-white">
        </form>
    </nav>
    <div class="flex ">
        <input type="checkbox" id="drawer-toggle" class="relative sr-only peer" checked>
        <label for="drawer-toggle" class="absolute top-0 left-0 inline-block p-4 transition-all duration-500 bg-indigo-500 rounded-lg peer-checked:rotate-180 peer-checked:left-64">
            <div class="w-6 h-1 mb-3 -rotate-45 bg-white rounded-lg"></div>
            <div class="w-6 h-1 rotate-45 bg-white rounded-lg"></div>


        </label>
        <div class="fixed top-0 left-0 z-20 w-64 h-full transition-all duration-500 transform -translate-x-full bg-white shadow-lg peer-checked:translate-x-0">
            <div class="px-6 py-4">
                <h2 class="text-lg font-semibold">Drawer</h2>
                <p class="text-gray-500">This is a drawer.</p>
            </div>
        </div>
    </div>
    {@render children()}

</div>