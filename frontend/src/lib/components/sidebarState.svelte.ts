let isExpanded = $state(false);
export const sidebarState = {
	get isExpanded() {
		return isExpanded;
	},
	toggleSidebar() {
		isExpanded = !isExpanded;
	}
};
