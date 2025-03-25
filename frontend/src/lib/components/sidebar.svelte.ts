// Definiere einen globalen Zustand für die Sidebar

export function createSidebarState() {
	let isExpanded = $state(false);
	return {
		isExpanded,
		toggleSidebar() {
			isExpanded = !isExpanded;
		}
	};
}
