import type { Component } from 'svelte';
import { z } from 'zod';

export const BlockTypeSchema = z.enum(['theory', 'quiz']);
export type BlockType = z.infer<typeof BlockTypeSchema>;

export const BlockGroupSchema = z.object({
	type: BlockTypeSchema,
	label: z.string()
});
export type BlockGroup = z.infer<typeof BlockGroupSchema>;

export const BlockSchema = z.object({
	uuid: z.string().uuid(),
	name: z.string(),
	type: BlockTypeSchema
});
export type Block = z.infer<typeof BlockSchema>;

export const BlockItemSchema = z.object({
	uuid: z.string().uuid(),
	name: z.string(),
	type: BlockTypeSchema,
	element: z.custom<Component>() // oder: z.custom<Component>()
});
export type BlockItem = z.infer<typeof BlockItemSchema>;

export const BLOCK_GROUPS: BlockGroup[] = [{ type: 'quiz', label: 'Quiz Blocks' }];
