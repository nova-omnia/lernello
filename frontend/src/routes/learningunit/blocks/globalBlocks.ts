import type { Component } from 'svelte';

export type BlockType = 'theory' | 'quiz';

export interface BlockGroup {
	type: BlockType;
	label: string;
}

export const BLOCK_GROUPS: BlockGroup[] = [{ type: 'quiz', label: 'Quiz Blocks' }];

export interface Block {
	uuid: string;
	name: string;
	type: BlockType;
}

export interface BlockItem {
	uuid: string;
	name: string;
	type: BlockType;
	element: Component;
}
