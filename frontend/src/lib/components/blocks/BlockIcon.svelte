<script lang="ts">
	import type { BlockType } from '$lib/models/block';
	import { BookOpen, FileText, CircleHelp, type IconProps } from 'lucide-svelte';
	import type { HTMLAttributes } from 'svelte/elements';

	interface BlockIconProps extends IconProps {
		iconType: BlockType;
		class?: string;
	}

	const { iconType, ...props }: BlockIconProps = $props();

	const iconMap = {
		theory: BookOpen,
		quiz: FileText,
		help: CircleHelp
	};
	const IconComponent = iconMap[iconType] || iconMap['help'];
</script>

<IconComponent class="text-surface-400-600 h-6 w-6" {...props} />
