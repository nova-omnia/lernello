import {
	ChangePasswordStatusSchema,
	type ChangePasswordStatus
} from '$lib/models/changePasswordData';
import { userRequest } from '$lib/api/apiClient';

export async function changePassword(changedPasswordUser: {
	newPassword: string;
}): Promise<ChangePasswordStatus> {
	const changePasswordRes = await userRequest(`/api/user/change-password`, 'POST', {
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			newPassword: changedPasswordUser.newPassword
		})
	});
	return ChangePasswordStatusSchema.parse(changePasswordRes);
}
