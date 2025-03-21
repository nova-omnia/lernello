import {
	ChangePasswordData,
	ChangePasswordStatus,
	ChangePasswordStatusSchema
} from '$lib/models/changePasswordData';
import { userRequest } from '$lib/api/apiClient';

export async function changePassword(
	changePasswordData: ChangePasswordData
): Promise<ChangePasswordStatus> {
	const changePasswordRes = await userRequest(`/api/change-password`, 'POST', {
		body: JSON.stringify(changePasswordData)
	});
	return ChangePasswordStatusSchema.parse(changePasswordRes);
}
