<script lang="ts">
	import LearningUnit from './learningUnit.svelte';

	let { data } = $props();
</script>

<LearningUnit blocks={data.blocks} />
