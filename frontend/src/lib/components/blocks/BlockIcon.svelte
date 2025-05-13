<script lang="ts">
	import type { BlockResType } from '$lib/schemas/response/BlockRes';
	import { BookOpen, FileText, CircleHelp, ListChecks, type IconProps } from 'lucide-svelte';

	interface BlockIconProps extends IconProps {
		iconType: BlockResType;
		class?: string;
	}

	const { iconType, ...props }: BlockIconProps = $props();

	const iconMap = {
		THEORY: BookOpen,
		MULTIPLE_CHOICE: ListChecks,
		QUESTION: FileText,
		help: CircleHelp
	};
	const IconComponent = iconMap[iconType] || iconMap['help'];
</script>

<IconComponent class="text-surface-400-600 h-6 w-6" {...props} />
