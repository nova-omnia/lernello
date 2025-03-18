<script lang="ts">
	import '../app.css';
	let { children } = $props();
</script>

<div>
	<nav class="flex items-center justify-between border-b border-gray-300 bg-gray-100 p-2.5">
		<div>
			<a href="/dashboard" class="mr-5 text-blue-500 no-underline hover:underline">Dashboard</a>
			<a href="/learningkit" class="mr-5 text-blue-500 no-underline hover:underline"
				>Learning Kits</a
			>
		</div>
		<form method="POST" class="pointer-events-none flex items-center opacity-50">
			<input
				type="text"
				placeholder="Search..."
				class="mr-2.5 rounded border border-gray-300 bg-white p-1 text-black"
			/>
		</form>
	</nav>
	<div class="flex">
		<input type="checkbox" id="drawer-toggle" class="peer sr-only relative" checked />
		<label
			for="drawer-toggle"
			class="absolute top-0 left-0 inline-block rounded-lg bg-indigo-500 p-4 transition-all duration-500 peer-checked:left-64 peer-checked:rotate-180"
		>
			<div class="mb-3 h-1 w-6 -rotate-45 rounded-lg bg-white"></div>
			<div class="h-1 w-6 rotate-45 rounded-lg bg-white"></div>
		</label>
		<div
			class="fixed top-0 left-0 z-20 h-full w-64 -translate-x-full transform bg-white shadow-lg transition-all duration-500 peer-checked:translate-x-0"
		>
			<div class="px-6 py-4">
				<h2 class="text-lg font-semibold">Drawer</h2>
				<p class="text-gray-500">This is a drawer.</p>
			</div>
		</div>
	</div>
	{@render children()}
</div>