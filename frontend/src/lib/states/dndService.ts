import { overrideItemIdKeyNameBeforeInitialisingDndZones } from 'svelte-dnd-action';

let isDndKeyOverridden = false;

export function initializeInstructorDnd() {
	if (!isDndKeyOverridden) {
		try {
			overrideItemIdKeyNameBeforeInitialisingDndZones('uuid');
			isDndKeyOverridden = true;
		} catch (error) {
			// This error implies a D&D zone was initialized before this function could run.
			// This could happen if another component initializes a D&D zone earlier.
			// Or, if the library's state is such that even the first call here is "too late".
			console.warn(
				'Failed to override D&D item ID key. It might be already set, or a D&D zone was initialized prematurely.',
				error
			);
			// If the key was already set to 'uuid' by some other means and this errors,
			// D&D might still work. If it wasn't set and this errors, D&D for instructors might fail.
		}
	}
}
