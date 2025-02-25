<script lang="ts">
	import { Accordion } from '@skeletonlabs/skeleton-svelte';
	// Icons
	import Club from 'lucide-svelte/icons/club';
	import Diamond from 'lucide-svelte/icons/diamond';
	import Heart from 'lucide-svelte/icons/heart';
	import Spade from 'lucide-svelte/icons/spade';

	const lorem =
		'Lorem ipsum dolor, sit amet consectetur adipisicing elit. Sit esse nisi eligendi fuga! Quas nisi repellat adipisci animi repellendus incidunt laborum sunt qui nesciunt, ducimus saepe sapiente sed ut labore. Lorem ipsum dolor, sit amet consectetur adipisicing elit. Sit esse nisi eligendi fuga! Quas nisi repellat adipisci animi repellendus incidunt laborum sunt qui nesciunt, ducimus saepe sapiente sed ut labore.';

	const value = $state(['club']);
</script>

<div class="mx-auto max-w-lg p-8">
	<h1 class="mb-4 text-4xl font-bold">This is Skeleton!</h1>
	<Accordion {value}>
		<Accordion.Item value="club">
			<!-- Control -->
			{#snippet lead()}<Club size={24} />{/snippet}
			{#snippet control()}Club{/snippet}
			<!-- Panel -->
			{#snippet panel()}{lorem}{/snippet}
		</Accordion.Item>
		<hr class="hr" />
		<Accordion.Item value="diamond">
			{#snippet lead()}<Diamond size={24} />{/snippet}
			{#snippet control()}Diamond{/snippet}
			{#snippet panel()}{lorem}{/snippet}
		</Accordion.Item>
		<hr class="hr" />
		<Accordion.Item value="heart" disabled>
			{#snippet lead()}<Heart size={24} />{/snippet}
			{#snippet control()}Heart (disabled){/snippet}
			{#snippet panel()}{lorem}{/snippet}
		</Accordion.Item>
		<hr class="hr" />
		<Accordion.Item value="spade">
			{#snippet lead()}<Spade size={24} />{/snippet}
			{#snippet control()}Spade{/snippet}
			{#snippet panel()}{lorem}{/snippet}
		</Accordion.Item>
	</Accordion>
</div>
<div class="mx-auto max-w-lg p-8">
	<h2 class="mb-4 text-2xl font-bold">Here are some form elements</h2>
	<form class="mx-auto w-full max-w-md space-y-4">
		<!-- Search -->
		<input class="input" type="search" placeholder="Search..." />
		<!-- Date Picker -->
		<label class="label">
			<span class="label-text">Date</span>
			<input class="input" type="date" />
		</label>
		<!-- File Input -->
		<label class="label">
			<span class="label-text">File Input</span>
			<input class="input" type="file" />
		</label>
		<!-- Range -->
		<label class="label">
			<span class="label-text">Range</span>
			<input class="input" type="range" value="75" max="100" />
		</label>
		<!-- Progress -->
		<label class="label">
			<span class="label-text">Progress</span>
			<progress class="progress" value="25" max="100"></progress>
		</label>
	</form>
</div>
