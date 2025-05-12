import { overrideItemIdKeyNameBeforeInitialisingDndZones } from 'svelte-dnd-action';

let isDndKeyOverridden = false;

export function initializeInstructorDnd() {
	if (!isDndKeyOverridden) {
		try {
			overrideItemIdKeyNameBeforeInitialisingDndZones('uuid');
			isDndKeyOverridden = true;
		} catch (error) {
			// This error implies a D&D zone was initialized before this function could run.
			console.warn(
				'Failed to override D&D item ID key. It might be already set, or a D&D zone was initialized prematurely.',
				error
			);
		}
	}
}
