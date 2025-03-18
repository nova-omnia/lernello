import { writable } from 'svelte/store';

// Definiere einen globalen Zustand für die Sidebar
export const isExpanded = writable(false);
