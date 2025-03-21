import {
	ChangedPasswordUser,
	ChangePasswordStatus,
	ChangePasswordStatusSchema
} from '$lib/models/changePasswordData';
import { userRequest } from '$lib/api/apiClient';

export async function changePassword(
	changedPasswordUser: ChangedPasswordUser
): Promise<ChangePasswordStatus> {
	const changePasswordRes = await userRequest(`/api/user/change-password`, 'POST', {
		body: JSON.stringify(changedPasswordUser)
	});
	return ChangePasswordStatusSchema.parse(changePasswordRes);
}
