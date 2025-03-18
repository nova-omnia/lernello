<script lang="ts">
    import '../app.css';

    let {children} = $props();
</script>

<div>
	<nav class="flex items-center justify-between border-b border-gray-300 bg-gray-100 p-2.5">
		<div>
			<a href="/dashboard" class="mr-5 text-blue-500 no-underline hover:underline">Dashboard</a>
			<a href="/learningkit" class="mr-5 text-blue-500 no-underline hover:underline">Learning Kits</a>
		</div>
		<form method="POST" class="pointer-events-none flex items-center opacity-50">
			<input
				type="text"
				placeholder="Search..."
				class="mr-2.5 rounded border border-gray-300 bg-white p-1 text-black"/>
		</form>
	</nav>
		<div class="flex flex-col items-center w-16 h-full overflow-hidden text-gray-700 bg-gray-100 rounded">
			<a class="flex items-center justify-center mt-3" href="#" aria-label="Lernello">
				<svg class="w-8 h-8 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
					 fill="currentColor">
					<path d="M11 17a1 1 0 001.447.894l4-2A1 1 0 0017 15V9.236a1 1 0 00-1.447-.894l-4 2a1 1 0 00-.553.894V17zM15.211 6.276a1 1 0 000-1.788l-4.764-2.382a1 1 0 00-.894 0L4.789 4.488a1 1 0 000 1.788l4.764 2.382a1 1 0 00.894 0l4.764-2.382zM4.447 8.342A1 1 0 003 9.236V15a1 1 0 00.553.894l4 2A1 1 0 009 17v-5.764a1 1 0 00-.553-.894l-4-2z"/>
				</svg>
			</a>
			<div class="flex flex-col items-center mt-3 border-t border-gray-300">
				<a class="flex items-center justify-center w-12 h-12 mt-2 rounded hover:bg-gray-300" href="#" aria-label="Home">
					<svg class="w-6 h-6 stroke-current" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
						 stroke="currentColor">
						<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
							  d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/>
					</svg>
				</a>
				<a class="flex items-center justify-center w-12 h-12 mt-2 rounded hover:bg-gray-300" href="#" aria-label="Learning Kit">
					<svg class="w-6 h-6 stroke-current" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
						 stroke="currentColor">
						<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
							  d="M8 7v8a2 2 0 002 2h6M8 7V5a2 2 0 012-2h4.586a1 1 0 01.707.293l4.414 4.414a1 1 0 01.293.707V15a2 2 0 01-2 2h-2M8 7H6a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2v-2"/>
					</svg>
				</a>
				<a class="flex items-center justify-center w-12 h-12 mt-2 rounded hover:bg-gray-300" href="#" aria-label="Statistics">
					<svg class="w-6 h-6 stroke-current" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
						 stroke="currentColor">
						<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
							  d="M16 8v8m-4-5v5m-4-2v2m-2 4h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"/>
					</svg>
				</a>
			</div>
			<div class="flex flex-col items-center mt-2 border-t border-gray-300">
				<a class="flex items-center justify-center w-12 h-12 mt-2 rounded hover:bg-gray-300" href="#" aria-label="Settings">
					<svg class="w-6 h-6 stroke-current" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
						 stroke="currentColor">
						<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
							  d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"/>
					</svg>
				</a>
				<a class="relative flex items-center justify-center w-12 h-12 mt-2 hover:bg-gray-300" href="#" aria-label="Notifications">
					<svg class="w-6 h-6 stroke-current" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
						 stroke="currentColor">
						<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
							  d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"/>
					</svg>
					<span class="absolute top-0 left-0 w-2 h-2 mt-2 ml-2 bg-indigo-500 rounded-full"></span>
				</a>
			</div>
			<a class="flex items-center justify-center w-16 h-16 mt-auto bg-gray-200 hover:bg-gray-300" href="#" aria-label="Profile">
				<svg class="w-6 h-6 stroke-current" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
					 stroke="currentColor">
					<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
						  d="M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0zm6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
				</svg>
			</a>
		</div>
	{@render children()}
</div>